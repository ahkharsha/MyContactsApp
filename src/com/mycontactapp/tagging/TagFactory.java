package com.mycontactapp.tagging;

import java.util.HashMap;
import java.util.Map;

/**
 * TagFactory
 * Implements the Flyweight Pattern for Tag objects to ensure memory efficiency.
 * It caches newly created tags and returns existing ones rather than instantiating
 * duplicates for identical string values.
 *
 * @author Developer
 * @version 1.0
 */
public class TagFactory {
    // Cache to hold shared tag instances (Flyweight pool)
    private static final Map<String, Tag> tagCache = new HashMap<>();

    /**
     * Retrieves an existing Tag from the cache or creates a new one if it doesn't exist.
     * @param name The name of the tag (e.g., "Work", "family")
     * @return The canonical shared Tag instance
     */
    public static Tag getTag(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty.");
        }
        
        // Normalize the key to make retrieval case-insensitive and whitespace safe
        String normalizedKey = name.trim().toLowerCase();
        
        // Check cache; if absent, compute and store
        return tagCache.computeIfAbsent(normalizedKey, Tag::new);
    }
    
    /**
     * Computes the current number of unique tags stored in memory.
     * @return Cache size
     */
    public static int getCacheSize() {
        return tagCache.size();
    }
}
