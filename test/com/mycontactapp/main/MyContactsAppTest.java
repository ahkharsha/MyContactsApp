package com.mycontactapp.main;

import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * MyContactsAppTest
 * Master Test Suite for the entire MyContactsApp project.
 * Automatically discovers and executes all JUnit 5 tests within the com.mycontactapp package structure.
 */
@Suite
@SelectPackages("com.mycontactapp")
@ExcludeClassNamePatterns(".*MyContactsAppTest.*")
public class MyContactsAppTest {
    // This class acts as a container for the @Suite and @SelectPackages annotations.
}
