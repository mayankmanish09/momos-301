package assignment1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javax.swing.JFrame;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.util.NeighborIterator;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class randomGraph2 {

	private static int i, j = 0;
	private static int similarEdges = 0;
	private static int libncon = 0;
	private static int triads = 0;

	private static BufferedReader br;

	public static void main(String[] args) throws IOException {
		try {

			// PARSING THE DATA INTO A GRAPH

			br = new BufferedReader(
					new FileReader(
							"C:\\Users\\pc\\Desktop\\acads\\CSP 301\\polblogs.gml"));
			String line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			boolean bool;
			if (line.equals("  directed 0")) {
				bool = false;
			} else {
				bool = true;
			}

			// MAKING TABLES TO STORE NODE DATA AND EDGE DATA

			Table nodeData = new Table();
			nodeData.addColumn("id", int.class);
			// nodeData.addColumn("label", String.class);
			nodeData.addColumn("value", int.class);
			// nodeData.addColumn("src", String.class);
			Table edgeData = new Table();
			// edgeData.addColumn("con", String.class);
			// edgeData.addColumn("source", int.class);
			// edgeData.addColumn("target", int.class);

			// MAKING THE GRAPH

			Graph g = new Graph(nodeData, bool);
			while (line != null) {
				if (line.equals("  node [")) {
					br.readLine();
					br.readLine();
					String value = br.readLine();
					br.readLine();
					Node p = g.addNode();

					// FILLING INTO THE NODE DATA TABLE

					p.setInt("id", i);
					// p.set("label",label.split("[\"]+")[1] );
					p.setInt("value",
							Integer.parseInt(value.split("[value ]+")[1]));
					// String[] s1 = source.split("[\" ,]+");
					// for(int i=1 ; i<s1.length; i++){
					// p.set("src", s1[i]);
					// }
					i++;
					// labelArray[i] = label.split("[\"]+")[1];
					// valueArray[i] = value.split("[\"]+")[1];
					line = br.readLine();
				}
				// MAKING EDGES OF THE GRAPH

				/*
				 * if (line.equals("  edge [")) { System.out.println("in");
				 * Random rand = new Random(); int s = rand.nextInt(i); int t =
				 * rand.nextInt(i - 1); g.addEdge(s, t); j++; }
				 */

				if (line.equals("  edge [")) {
					// System.out.println(j);
					br.readLine();
					br.readLine();
					Random rand = new Random();
					int s = rand.nextInt(i);
					int t = rand.nextInt(i - 1);
					g.addEdge(s, t);
					
					 int s1 = g.getNode(s).getInt("value"); 
					 int t1 =g.getNode(t).getInt("value"); 
					 if(s1 == 0 && t1 == 0)similarEdges++; 
					 else if(s1 == 1 && t1 == 1)similarEdges++; 
					 else libncon++; 
					 
					j++;
				}
				line = br.readLine();
				// System.out.println(line);
			}

			// FILLING INTO EDGE DATA TABLE


			/*for (int k = 0; k < j; k++) { 
				Edge edge = g.getEdge(k);
				String s1= (String) edge.getSourceNode().get("value"); 
				String s2 =(String) edge.getTargetNode().get("value"); 
				if (s1.equals("c") && s2.equals("c")) { 
					edge.set("con", "a"); 
					similarEdges++; } 
				else if(s1.equals("l") && s2.equals("l")) { 
					edge.set("con", "b");
					similarEdges++; 
				} 
				else { 
					edge.set("con", "c"); 
					if (s1.equals("n") && s2.equals("n")) 
						similarEdges++; 
					if (s1.equals("l") && s2.equals("c")) libncon++; }
			} */
			/*for (int d = 0; d < i; d++) { //System.out.println("1"); for
				(int b = 0; b < i; b++) { //System.out.println("2"); for (int c =
					0; c < i; c++) { System.out.println(d+" "+b+" "+c); if
					((g.getEdge(d, b) != -1 || g.getEdge(d, b) != -1) &&
							(g.getEdge(b, c) != -1 || g .getEdge(c, b) != -1) &&
							(g.getEdge(c, d) != -1 || g .getEdge(d, c) != -1)) { triads++; }
					} } }*/


			/*for (int d = 0; d < i; d++) {
				NeighborIterator it = (NeighborIterator) (g.neighbors(g.getNode(d)));
				Node[] a = new Node[1000];
				int b = 0;
				while (it.hasNext() && b < 1000) {
					a[b] = (Node)it.next();
					b++;
				}
				for(int c = 0; c < b; c++){
					for(int d1 = c+1 ; d1 < b; d1++){
						System.out.println(a[c]);
						int s = a[c].getInt("id")-1;
						int t = a[d].getInt("id")-1;
						if(g.getEdge(s, t) != -1){
							triads++;
						}
					}
				}
			}*/
			float triad=0;
			for(int a=0;a<i;a++){
				Node x=g.getNode(a);
				for(int b=0;b<i;b++){
					Node y=g.getNode(b);
					if(g.getEdge(x, y)!=null){
						for(int c=0;c<i;c++)						{
							if(!(c==b)){
								Node z1=g.getNode(c);
								if(g.getEdge(z1,x)!=null)
									triad++;
							}
							else continue;	
						}
					}
				}
			}

			System.out.println("The number of edges between nodes of the same type = "
					+ similarEdges);
			System.out.println("the number of edges between nodes of 'l' and 'c' type = "
					+ libncon);
			System.out.println("Total number of edges = " + j);
			System.out.println("required ratio = " + (float) similarEdges / j);
			float nc2 = (i * (i-1)) / 2;
			System.out.println("number of triads = "+ (int)triad);
			float cluster_coeff = triad / nc2;
			System.out.println("cluster coefficient = "+cluster_coeff);
			// SETTING UP THE VISUALISATION

			/*
			 * Visualization vis = new Visualization(); vis.add("graph", g);
			 * LabelRenderer r = new LabelRenderer("id"); r.setRoundedCorner(8,
			 * 8); vis.setRendererFactory(new DefaultRendererFactory(r));
			 * 
			 * // MAKING PALETTES FOR USE FOR DATACOLORACTION
			 * 
			 * int[] palette = new int[] { ColorLib.rgb(255, 180, 180),
			 * ColorLib.rgb(190, 190, 255), ColorLib.rgb(190, 255, 180) }; int[]
			 * palette2 = new int[] { ColorLib.rgb(255, 180, 180),
			 * ColorLib.rgb(190, 190, 255), ColorLib.gray(150) };
			 * 
			 * // ADDING DATA COLR ACTION FOR NODE AND EDGE DATA; // SETTING UP
			 * HIGHLIGHT FOR NEIGHBOR HIGHLIGHT
			 * 
			 * DataColorAction fill = new DataColorAction("graph.nodes",
			 * "value", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
			 * fill.add(VisualItem.FIXED, ColorLib.rgb(255, 100, 100));
			 * fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));
			 * ColorAction text = new ColorAction("graph.nodes",
			 * VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0)); // ColorAction
			 * edges = new // ColorAction("graph.edges",VisualItem.STROKECOLOR,
			 * // ColorLib.gray(200)); DataColorAction edges = new
			 * DataColorAction("graph.edges", "con", Constants.NOMINAL,
			 * VisualItem.STROKECOLOR, palette2);
			 * 
			 * // MAKING ACTION LIST TO IMPLEMENT THE ACTIONS
			 * 
			 * ActionList color = new ActionList(); color.add(fill);
			 * color.add(text); color.add(edges);
			 * 
			 * // MAKING AN ACTION LIST WHICH WILL WORK INFINITELY
			 * 
			 * ActionList layout = new ActionList(Activity.INFINITY); // FORCED
			 * DIRECTED LAYOUT FOR MAKING CLUSTERS layout.add(new
			 * ForceDirectedLayout("graph")); layout.add(fill); layout.add(new
			 * RepaintAction()); vis.putAction("color", color);
			 * vis.putAction("layout", layout);
			 * 
			 * // SETTING UP THE DISPLAY
			 * 
			 * Display display = new Display(vis); display.setSize(720, 500); //
			 * ADDING DRAG, ZOOM, PAN, CONTROLS display.addControlListener(new
			 * DragControl()); display.addControlListener(new PanControl());
			 * display.addControlListener(new ZoomControl());
			 * display.addControlListener(new WheelZoomControl());
			 * display.addControlListener(new ZoomToFitControl());
			 * display.addControlListener(new NeighborHighlightControl());
			 * display.addControlListener(new FinalControlListener());
			 * 
			 * // SETTING UP AND RUNNING THE LAYOUT
			 * 
			 * JFrame frame = new JFrame("ass1");
			 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 * frame.add(display); frame.pack(); frame.setVisible(true);
			 * vis.run("color"); vis.run("layout");
			 */

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
