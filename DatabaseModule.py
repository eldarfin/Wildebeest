# -*- coding: utf-8 -*-
"""
Created on Mon Apr 23 16:54:53 2018

@author: taha
"""
import csv
from tfidf import tfidf 
import heapq
stopWords = []
class Candidate:
   clue=""
   answer =""
   confidence = 0.0
   
   def __lt__(self,other):
       return self.confidence < other.confidence
   
   def __gt__(self,other):
       return self.confidence > other.confidence
    
   def __eq__(self,other):
       return self.confidence == other.confidence
    
   def __nq__(self,other):
       return self.confidence != other.confidence
   
   def __str__(self):
       return  self.answer +" "+ str(self.confidence)+" "
   def __repr__(self):
    return self.__str__()
   def __init__(self,clue, answer, confidence):
      self.clue = clue
      self.answer = answer
      self.confidence = confidence
      
     
def Nearest(sentence,idf,num,answer,out):
    candidates= []
    c1 = Candidate("","",0.0)
    for i in range (10):
        heapq.heappush(candidates,c1)
    
    indexes,letters = foundLetters(answer)
    stop = set(stopWords)
    sentenceSet = set(sentence.split(" ")) - stop
    maxConfidence = 0
    for word in sentenceSet:
        maxConfidence = maxConfidence + idf[word]
    minP = 0
    selectedClues = ["","","","","","","","","",""]
    for i in range(len(clues)):
        if(len(answers[i]) != num):
            continue

        if(clues[i] in selectedClues):
            continue
#Checks if the given letters to answer fit with the current answer
        lettersFit = True
        for j in range(len(indexes)):
            if answers[i][indexes[j]] != letters[j]:
                lettersFit = False
                break
        if lettersFit is False:
            continue
#        print("Left Clue Count: "+ str(len(clues)-i) )
        p = set(clues[i].split(" ")) & sentenceSet
        score = 0
        for common in p:
            score = score + idf[common]
        if(score >= minP ):     
#            print("before", candidates)
#            print(answers[i]," my score is",score)
#            print("least score is",minP)
            curCan = Candidate(clues[i],answers[i],score/maxConfidence)
            selectedClues.append(clues[i])
            removedQ = heapq.heappushpop(candidates,curCan)
            selectedClues.remove(removedQ.clue)
            
#            print("pushed and popped")
#            print("after",candidates)
            minP = candidates[0].confidence
    results= []
    for i in range(10):
        cd = heapq.heappop(candidates)
        if cd.confidence != 0:
            results.append(str(cd))
    results.reverse()
    for item in results:
        out.write(item)
#    for outputCand in result:
#        out.write(str(outputCand)+" ")
def foundLetters(soFarAnswer):
    indexes = []
    letters = []
    for i in range(len(soFarAnswer)):
        if(soFarAnswer[i] != "0"):
            indexes.append(i)
            letters.append(soFarAnswer[i])
    return indexes, letters
questions = []
soFarAnswers = []
i = 0
f = open("p.txt","r")
for line in f:
    if(i%2==0):
        questions.append(line[:-1])
    else:
        soFarAnswers.append(line[:-1])
    i = i+1
f = open("stops.txt")
for stop in f:
    stopWords.append(stop[:-1])
f = open("clues.csv")
reader = csv.reader(f,delimiter=',')
clues =[]
answers = []
for row in reader:
    if(len(row[14]) <= 5):
        clues.append(row[13].lower())
        answers.append(row[14].lower())
f.close()
question = "its eaten with slices"
num = 5
answerPartial="00000"
idf, tf = tfidf(clues)
out = open("p_data.txt","w")
for i in range(len(questions)):
    Nearest(questions[i].lower(),idf,len(soFarAnswers[i]),soFarAnswers[i].lower(),out)
    out.write("\n")
out.close()
