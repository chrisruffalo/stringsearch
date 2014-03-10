package com.github.chrisruffalo.stringsearch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.github.chrisruffalo.stringsearch.Radix;

public class Dictionary {

	public static void load(Radix<String> toLoad, boolean insertKeysAsValues) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("dictionary.txt")));
		
		String line;
		//int size = 0;
		//int characters = 0;
		try {
			line = reader.readLine();
			while(line != null) {
				//size++;
				//characters += line.length();
				if(insertKeysAsValues) {
					toLoad.put(line, String.valueOf(line.hashCode()));
				} else {
					toLoad.put(line);
				}				
				line = reader.readLine();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void load(Radix<String> toLoad) {
		Dictionary.load(toLoad, false);		
	}
	
	public static List<String> load() {
		List<String> results = new LinkedList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("dictionary.txt")));
		
		String line;
		try {
			line = reader.readLine();
			while(line != null) {
				results.add(line);
				line = reader.readLine();
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
