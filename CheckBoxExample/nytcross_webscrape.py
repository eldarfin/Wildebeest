import html_getter as hg
from bs4 import BeautifulSoup as soup

page_html = hg.get_html()
page_soup = soup(page_html, "html.parser")

containers = page_soup.findAll("span",{"class":"Clue-text--3lZl7"})
rects = page_soup.findAll("rect",{"class":"Cell-block--1oNaD"})
answerRects = page_soup.findAll("text",{"text-anchor":"middle"})

question = []
rectCoordinates= []

for i in range(10):
	question.append(containers[i].text)
for i in range(len(rects)):
	x = rects[i]["x"]
	y = rects[i]["y"]
	rectCoordinates.append(int(float(x)/100) + 5*int(float(y)/100))

s = '0 {'
for i in range(len(rectCoordinates)):
	s += str(rectCoordinates[i]) + ','

s = s[:-1]
s += '} '

for i in range(len(question)):
	s += question[i] + ',,'
for i in range(len(answerRects)):
	s += answerRects[i].text.lower()

open('current_puzzle.txt', 'w').close()
with open('current_puzzle.txt', "w+") as f:
	f.write(s)
