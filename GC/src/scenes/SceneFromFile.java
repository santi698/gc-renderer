package scenes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.SimpleBody;
import model.cameras.Camera;
import model.light.Light;
import model.light.PointLight;
import model.materials.Material;
import model.materials.Metal2;
import model.shapes.Mesh;
import model.shapes.Shape;
import model.shapes.sphere.Sphere;
import model.texture.ImageTexture;
public class SceneFromFile /* extends Scene*/ {
	
	Map<String,Material> materials;
	List<Light> lights;
	List<Body> bodies;
	Camera camera;
	int AASamples = 32;
	
	public SceneFromFile() throws FileNotFoundException{
		
		bodies = new ArrayList<Body>();
		lights = new ArrayList<Light>();
		materials = new HashMap<String,Material>();
		
		materialParser("C:\\Users\\Dam\\Desktop\\untitled\\Scene\\00001\\LuxRender-Materials.lxm");
		geometryParser("C:\\Users\\Dam\\Desktop\\untitled\\Scene\\00001\\LuxRender-Geometry.lxo");
		cameraParser("C:\\Users\\Dam\\Desktop\\DemoScene.lxs");
		lightsParser("C:\\Users\\Dam\\Desktop\\DemoScene.lxs");
	}
	
	
	public void lightsParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		
		String lightType = "";
		
		String currentLine;
		
		boolean inLight = false;
		
		while(in.hasNextLine()){
			currentLine = in.nextLine();
			
			if(currentLine.startsWith("AttributeBegin")){
				inLight = true;
			}else if (currentLine.startsWith("AttributeEnd")){
				
				lights.add(GetLightFromArguments(lightType,arguments));
				arguments.clear();
				lightType= "";
				inLight = false;
			}			
			
			if(inLight){
				if(currentLine.startsWith("AreaLightSource")){
					
					int firstQuote= currentLine.indexOf('"') + 1;
					int lastQuote= currentLine.lastIndexOf('"');
					lightType = currentLine.substring(firstQuote,lastQuote);
					
				}else if(currentLine.startsWith("\t")){
		
					ParseArgument(currentLine,arguments);
				
				}
			}
		}
		
		in.close();
		return;	
	}	
	
	public Light GetLightFromArguments(String lightType, Map<String,Object> arguments){
		
		Light light = null;
		
		float intensity = (float) arguments.get("gain");
		
		switch(lightType){
			case "point":
				//The color of the light.
				Color3f color = (Color3f) arguments.get("I");
				
				//The location of the point light.
				List<Double> fromList = (List<Double>) arguments.get("from/to");
				Point3d from = new Point3d(fromList.get(0),fromList.get(1),fromList.get(2));
				
				light = new PointLight(from, color, intensity);
				
				break;
			case "infinite":
				//The color of the light.
				Color3f color2 = (Color3f) arguments.get("L");
				
				//The suggested number of shadow samples when computing illumination from the given light.
				int nsamples = ((List<Integer>) arguments.get("nsamples")).get(0);
				
				//light = new Infinite ?
				
				break;
			case "distant":
				//The color of the light.	
				Color3f color3 = (Color3f) arguments.get("L");

				//The two points defining the light direction. Default is down the z axis.
				List<Double> fromTo = (List<Double>) arguments.get("from/to");
				Point3d from2 = new Point3d(fromTo.get(0),fromTo.get(1),fromTo.get(2));
				Point3d to = new Point3d(fromTo.get(3),fromTo.get(4),fromTo.get(5));
				
				//Half angle of the light source in radians. Must be > 0 for soft shadows
				float theta = (float) arguments.get("theta");
				
				//light = new Distant ?		
				
				break;
			default:
				System.out.println(lightType + " not supported.");
				break;
		}
		
		return light;
	}

	public void cameraParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
				
		Point3d position = null;
		Point3d lookAt = null;
		Point3d up = null;
		double fov = 0;
		int xRes = 0;
		int yRes = 0;
		
		String currentLine;
		
		while(in.hasNextLine()){
			currentLine = in.nextLine();
			if(currentLine.startsWith("LookAt")){
				String[] args = currentLine.split(" ");
				position = new Point3d(Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
				lookAt = new Point3d(Double.parseDouble(args[4]),Double.parseDouble(args[5]),Double.parseDouble(args[6]));
				up = new Point3d(Double.parseDouble(args[7]),Double.parseDouble(args[8]),Double.parseDouble(args[9]));
				
			}else if(currentLine.startsWith("\t\"float fov\"")){
				int firstBracket= currentLine.indexOf('[') + 1;
				int lastBracket= currentLine.lastIndexOf(']');
				fov = Double.parseDouble(currentLine.substring(firstBracket,lastBracket));
			}else if(currentLine.startsWith("\t\"integer xresolution\"")){
				int firstBracket= currentLine.indexOf('[') + 1;
				int lastBracket= currentLine.lastIndexOf(']');
				xRes = Integer.parseInt(currentLine.substring(firstBracket,lastBracket));
			}else if(currentLine.startsWith("\t\"integer yresolution\"")){
				int firstBracket= currentLine.indexOf('[') + 1;
				int lastBracket= currentLine.lastIndexOf(']');
				yRes = Integer.parseInt(currentLine.substring(firstBracket,lastBracket));
				break;
			}
		}

		
		//camera = new PinholeCamera();
		
		in.close();
		return;	
	}
	
	public void geometryParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		in.nextLine();in.nextLine(); //clears "# Geometry File" and the next line
		
		Material material = null;
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		
		Vector3d position = null;
		Vector3d rotation = null;
		double scale = 0.0d;
		
		String shapeType = "";

		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			
			if(currentLine.startsWith("Transform")){
				String[] values = currentLine.substring(11,currentLine.length()-1).split(" ");
								
				scale = Double.parseDouble(values[0]);
				//rotation = ;
				position = new Vector3d(Double.parseDouble(values[12]),Double.parseDouble(values[13]),Double.parseDouble(values[14]));

			}else if(currentLine.startsWith("NamedMaterial")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				material = materials.get(currentLine.substring(firstQuote,lastQuote));
			
			}else if(currentLine.startsWith("Shape")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				shapeType = currentLine.substring(firstQuote,lastQuote);
				
			}else if(currentLine.startsWith("\t")){
				
				ParseArgument(currentLine,arguments);
			
			}else if(currentLine.startsWith("AttributeEnd")){
		
				bodies.add(new SimpleBody(GetShapeFromArguments(shapeType,arguments,position,rotation,scale),material));
				System.out.println("added "+ shapeType  + " with material "+ material);
				material = null;
				arguments.clear();
				shapeType="";
			}
			
		}
		
		in.close();
		return;	
	}
	
	public Shape GetShapeFromArguments(String shapeType, Map<String,Object> arguments, Vector3d position, Vector3d rotation, double scale){
		
		Shape shape = null;
		
		switch(shapeType){
		case "mesh":
			List<Integer> triindices = (List<Integer>)arguments.get("triindices");
			List<Double> P = (List<Double>)arguments.get("P");
			
			shape = new Mesh(position,rotation,scale,triindices,P);
			break;
		case "plane":
			//Vector3 de normales
			List<Double> N = (List<Double>)arguments.get("N");
			// shape = new Plane();
			break;
		case "box":
			float width = (float)arguments.get("width");
			float height = (float)arguments.get("height");
			float depth = (float)arguments.get("depth");
			//shape = new Box();
			break;
		case "sphere":
			float radius = (float)arguments.get("radius");
			
			//shape = new Sphere(radius);
			
			break;
		default:
			System.out.println(shapeType + " not supported.");
		}
		
		return shape;
	}
	
	public void materialParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		in.nextLine();in.nextLine(); //clears "# Materials File" and the next line
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		String currentMaterialName = "";
		
		ImageTexture texture = null;
		
		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			
			if(currentLine.startsWith("MakeNamedMaterial")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				currentMaterialName = currentLine.substring(firstQuote,lastQuote);
			
			}else if(currentLine.startsWith("\t")){
				
				ParseArgument(currentLine,arguments);
				
			} else if(currentLine.startsWith("Texture")){

			
			} else if( currentLine.equals("") && !currentMaterialName.equals("")){
					materials.put(currentMaterialName, GetMaterialFromArguments(arguments,texture));
					currentMaterialName = "";
					texture = null;
					arguments.clear();
			}
					
		}
		
		in.close();
		return;	
	}
	
	public Material GetMaterialFromArguments(Map<String,Object> arguments, ImageTexture texture){
		
		Material material = null;
		
		String type = (String)arguments.get("type");
		switch(type){
			case "matte":
				Color3f Kd = (Color3f)arguments.get("Kd");
				float sigma = (float)arguments.get("sigma");
				// material = new Matte();
				break;
			case "mirror":
				float _filmindex = (float)arguments.get("filmindex");
				Color3f _Kr = (Color3f)arguments.get("Kr");
				//material = new Mirror();
				break;
			case "glass":
				Color3f Kr = (Color3f)arguments.get("Kr");
				Color3f Kt = (Color3f)arguments.get("Kt");
				float index = (float)arguments.get("index");
				float filmindex = (float)arguments.get("filmindex");
				// material = new Glass();
				break;
			case "metal2":
				float uroughness = (float)arguments.get("uroughness");
				float vroughness = (float)arguments.get("vroughness");
				material = new Metal2(texture,uroughness);
				break;
			default:
				System.out.println(type + " Material not supported");
				break;
		}
		
		return material;
		
	}	
	
	public void ParseArgument(String line, Map<String,Object> arguments){
		String[] splitedLine = line.split("\"");
		
		String ArgumentType = splitedLine[1].split(" ")[0];
		String ArgumentName = splitedLine[1].split(" ")[1];
		
		String ArgumentValue = line.substring(ArgumentType.length() + ArgumentName.length() + 6,line.length()-1);
		
		switch(ArgumentType){
			case "bool":
				
				if(ArgumentValue.equals("true"))
					arguments.put(ArgumentName, true);
				else
					arguments.put(ArgumentName, false);
				break;
				
			case "float":
				
				arguments.put(ArgumentName,Float.parseFloat(ArgumentValue));
				break;
				
			case "string":
				
				arguments.put(ArgumentName,ArgumentValue.substring(1, ArgumentValue.length()-1));
				break;
				
			case "color":
				float[] floats = new float[3];
				int i = 0;
				for(String s: ArgumentValue.split(" "))
					floats[i++] = Float.parseFloat(s);
				
				Color3f color = new Color3f(floats[0],floats[1],floats[2]);
				
				arguments.put(ArgumentName,color);

				break;
			case "integer":
				List<Integer> ints = new ArrayList<Integer>();
				for(String s: ArgumentValue.split(" "))
					ints.add(Integer.parseInt(s));
				
				arguments.put(ArgumentName,ints);

				break;
			case "point":
				List<Double> doubles = new ArrayList<Double>();
				for(String s: ArgumentValue.split(" "))
					doubles.add(Double.parseDouble(s));
				
				arguments.put(ArgumentName,doubles);
				break;
			case "normal":
				List<Double> _doubles = new ArrayList<Double>();
				for(String s: ArgumentValue.split(" "))
					_doubles.add(Double.parseDouble(s));
				
				arguments.put(ArgumentName,_doubles);
				
				break;
			default:
				System.out.println(ArgumentType + " not supported.");
				break;
		}
	}
	
}
