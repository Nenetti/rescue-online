package module;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NodeFX {
	
	public Parent root;
	private HashMap<String, Node> nodeMap=new HashMap<>();
	private HashMap<String, Tab> tabMap=new HashMap<>();
	private HashMap<String, MenuItem> menuMap=new HashMap<>();
	
	public NodeFX(Parent root) {
		this.root=root;
		getAllNodes(root);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Node> HashSet<T> getAllNode(Class<T> classT){
		return new HashSet<>(nodeMap.values().stream()
				.filter(t -> t.getClass()==classT)
				.map(t -> (T)t)
				.collect(Collectors.toSet()));
	} 
	
	public HashMap<String, Node> getNodeMap() {
		return nodeMap;
	}
	
	public Node getNode(String id) {
		return nodeMap.get(id);
	}
	public Label getLabel(String id) {
		return (Label)nodeMap.get(id);
	}
	public AnchorPane getAnchorPane(String id) {
		return (AnchorPane)nodeMap.get(id);
	}
	public TabPane getTabPane(String id) {
		return (TabPane)nodeMap.get(id);
	}
	public SplitPane getSplitPane(String id) {
		return (SplitPane)nodeMap.get(id);
	}
	public ProgressBar getProgressBar(String id) {
		return (ProgressBar)nodeMap.get(id);
	}
	public ImageView getImageView(String id) {
		return (ImageView)nodeMap.get(id);
	}
	public Tab getTab(String id) {
		return tabMap.get(id);
	}
	public MenuItem getMenuItem(String id) {
		return menuMap.get(id);
	}
	public Button getButton(String id) {
		return (Button)nodeMap.get(id);
	}
	
	
	
	
	
	public static NodeFX readFXML(String path) throws Exception {
		return new NodeFX(FXMLLoader.load(new File(path).toURI().toURL()));
	}
	
	private void getAllNodes(Node node){
		List<Node> nowNode=new ArrayList<>();
		List<Node> nextNode=new ArrayList<>();
		nowNode.add(node);
		while(nowNode!=null&&!nowNode.isEmpty()) {
			for(Node n:nowNode) {
				if(n!=null) {
					List<Node> ns=getChildren(n);
					if(ns!=null&&!ns.isEmpty()) {
						nextNode.addAll(ns);
					}
				}
			}
			setNodes(nowNode);
			nowNode=nextNode;
			nextNode=new ArrayList<>();
		}
	}
	
	private void setNodes(List<Node> nodes) {
		for(Node node:nodes) {
			if(node!=null&&node.getId()!=null) {
				nodeMap.put(node.getId(), node);
			}
		}
	}
	
	private void setAllTab(TabPane tabPane){
		for(Tab tab:tabPane.getTabs()) {
			if(tab.getId()!=null) {
				this.tabMap.put(tab.getId(), tab);
			}
		}
	}
	
	private void setAllMenuItems(MenuBar node){
		for(MenuItem item:node.getMenus()) {
			if(item.getId()!=null) {
				this.menuMap.put(item.getId(), item);
			}
		}
	}

	private List<Node> getChildren(Node node){
		if(node instanceof VBox) {
			return ((VBox)node).getChildren();
		}else if(node instanceof HBox) {
			return ((HBox)node).getChildren();
		}else if(node instanceof SplitPane) {
			return ((SplitPane)node).getItems();
		}else if(node instanceof ScrollPane) {
			List<Node> n=new ArrayList<>();
			n.add(((ScrollPane)node).getContent());
			return n;
		}else if(node instanceof AnchorPane) {
			return ((AnchorPane)node).getChildren();
		}else if(node instanceof TitledPane) {
			List<Node> n=new ArrayList<>();
			n.add(((TitledPane)node).getContent());
			return n;
		}else if(node instanceof ToolBar) {
			return ((ToolBar)node).getItems();
		}else if(node instanceof TabPane) {
			setAllTab((TabPane)node);
			return getChildrenTab((TabPane)node);
		}else if(node instanceof MenuBar) {
			setAllMenuItems((MenuBar)node);
		}
		return null;
	}
	
	private List<Node> getChildrenTab(TabPane menu){
		List<Node> result=new ArrayList<>();
		for(Tab object:menu.getTabs()) {
			result.add(object.getContent());
		}
		return result;
	}
	
	public void resize(Node node, double width,double height) {
		node.minWidth(width);
		node.prefWidth(width);
		node.maxWidth(width);
		node.minHeight(height);
		node.prefHeight(height);
		node.maxHeight(height);
	}
}
