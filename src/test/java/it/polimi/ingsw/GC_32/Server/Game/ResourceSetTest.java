package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eclipsesource.json.JsonObject;

public class ResourceSetTest{
	public ResourceSet resourceSet;
	
	@Test
	public void checkResourceSetNotNull(){
		this.resourceSet = new ResourceSet();
		assertNotNull(this.resourceSet.getResourceSet());
	}
	
	@Test
	public void checkResourceSetting(){
		this.resourceSet = new ResourceSet();
		this.resourceSet.setResource("WOOD", 10);
		assertEquals(this.resourceSet.getResouce("WOOD"), 10);
	}
	
	@Test
	public void checkResourceIntAdding(){
		this.resourceSet= new ResourceSet();
		this.resourceSet.setResource("WOOD", 10);
		this.resourceSet.addResource("WOOD", 5);
		assertEquals(this.resourceSet.getResouce("WOOD"), 15);
		this.resourceSet.addResource("STONE", 17);
		assertEquals(this.resourceSet.getResouce("STONE"), 17);
	}
	
	@Test
	public void checkResourceSetAdding(){
		this.resourceSet= new ResourceSet();
		this.resourceSet.setResource("WOOD", 10);
		ResourceSet newResource = new ResourceSet();
		newResource.setResource("WOOD", 5);
		this.resourceSet.addResource(newResource);
		assertEquals(this.resourceSet.getResouce("WOOD"), 15);
	}
	
	@Test
	public void checkResourceSetJson(){
		JsonObject testObject = new JsonObject();
		testObject.add("WOOD", 22);
		this.resourceSet = new ResourceSet(testObject);
		assertEquals(this.resourceSet.getResouce("WOOD"), 22);
	}
	
	@Test
	public void checkToStringNotNull(){
		this.resourceSet= new ResourceSet();
		assertNotNull(this.resourceSet.toString());
	}
}