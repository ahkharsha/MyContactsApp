package com.mycontactapp.tagging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagFactoryTest {

    @Test
    public void testTagFlyweightSharing() {
        System.out.println("Running Test: TagFactory ensures memory reuse (Flyweight Pattern)");
        
        Tag t1 = TagFactory.getTag("work");
        Tag t2 = TagFactory.getTag("Work");
        Tag t3 = TagFactory.getTag(" WORK ");
        
        // Assert reference equality (they point to the EXACT same object in memory)
        assertSame(t1, t2, "Tags with matching strings case-insensitive should be the exact same object");
        assertSame(t1, t3, "Tags with matching strings ignoring whitespace should be the exact same object");
        
        System.out.println("Test Passed.");
    }
    
    @Test
    public void testTagFactoryCacheSize() {
        System.out.println("Running Test: TagFactory tracks unique instances accurately");
        
        int initialSize = TagFactory.getCacheSize();
        
        TagFactory.getTag("NewTag1");
        TagFactory.getTag("newtag1"); // should reuse
        TagFactory.getTag("NewTag2");
        
        assertEquals(initialSize + 2, TagFactory.getCacheSize(), "Cache size should only increase by unique tag counts");
        
        System.out.println("Test Passed.");
    }
}
