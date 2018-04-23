# -*- coding: utf-8 -*-
"""
Created on Sat Apr 21 22:21:52 2018

@author: onur
"""
import math 
import numpy
from scipy import spatial
def tfidf(poems):
	idfDictionary = dict()
	tfDictionaries = list()
	for poem in poems:
		tf = dict()
		for term in poem.split(" "):
			tf[term] = tf.get(term, 0)+1
		termsList = tf.keys()
		for term in termsList:
			idfDictionary[term] = idfDictionary.get(term, 0)+1
		termCounts = tf.values()
		maxItemCount = max(termCounts)
		for key, val in tf.items():
			tf[key] = 0.5 + 0.5 * (val/maxItemCount)
		tfDictionaries.append(tf)
	for key, value in idfDictionary.items():
		idfDictionary[key] = math.log(len(poems)/value)
	result = []
	result.append(idfDictionary)
	result.append(tfDictionaries)
	return result
