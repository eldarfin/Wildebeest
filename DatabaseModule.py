# -*- coding: utf-8 -*-
"""
Created on Mon Apr 23 16:54:53 2018

@author: taha
"""
import csv
from tfidf import tfidf 
stopWords = []
def Nearest(sentence,idf):
    stop = set(stopWords)
    sentenceSet = set(sentence.split(" ")) - stop
    maxP = 0
    resultingClue = clues[0]
    resultingAnswer = answers[0]
    print(len(clues))
    for i in range(len(clues)):
#        print("Left Clue Count: "+ str(len(clues)-i) )
        p = set(clues[i].split(" ")) & sentenceSet
        score = 0
        for common in p:
            score = score + idf[common]
        if(score > maxP ):     
            maxP = score
            resultingAnswer = answers[i]
            resultingClue = clues[i]
            
    print(resultingClue)
    print(resultingAnswer),


f = open("stops.txt")
for stop in f:
    stopWords.append(stop)
f = open("clues.csv")
reader = csv.reader(f,delimiter=',')
clues =[]
answers = []
for row in reader:
	if(len(row[14]) <= 5):
	    clues.append(row[13].lower())
	    answers.append(row[14].lower())
f.close()
question = "kind of cake made in a ring-shaped pan"
idf, tf = tfidf(clues)
print("idf done")
Nearest(question,idf)
