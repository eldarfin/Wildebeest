import requests
from lxml import html
from bs4 import BeautifulSoup
import nltk
import operator
import string

def RepresentsInt(s):
    try:
        int(s)
        return True
    except ValueError:
        return False

def IsAllZeros(s):
    for c in s:
        if c != '0':
            return False
    return True

nltk.download('punkt')
stopwords = ['i','me','my','myself','we','our','ours','ourselves','you','your','yours','yourself','yourselves','he','him','his','himself','she','her','hers','herself','it','its','itself','they','them','their','theirs','themselves','what','which','who','whom','this','that','these','those','am','is','are','was','were','be','been','being','have','has','had','having','do','does','did','doing','a','an','the','and','but','if','or','because','as','until','while','of','at','by','for','with','to','from','then','once','here','there','when','where','why','how','all','any','such','no','nor','not','only','own','same','so','than']

def get_ans(q, a):
    url = "http://www.google.com/search?q="
    question = nltk.tokenize.word_tokenize(q)
    for w in question:
        url = url + w + '+'
    url = url + 'crossword'
    r = requests.get(url)
    soup = BeautifulSoup(r.text, 'html.parser')
    links = []
    for item in soup.find_all('h3', attrs={'class' : 'r'}):
        links.append(item.a['href'][7:])
    texts = []
    punctuations = '''!()-[]{};:'"\,<>./?@#$%^&*_~'''
    for link in links:
        try:
            page = requests.get(link)
            soup = BeautifulSoup(page.content, 'html.parser')
            for script in soup(["script", "style"]):
                script.extract()
            no_punct = ""
            my_str = soup.get_text()
            for char in my_str:
               if char not in punctuations:
                   no_punct = no_punct + char
            tokens = nltk.tokenize.word_tokenize(no_punct)
            tags = soup.find_all('a')
            for t in tags:
                no_punct = ""
                my_str = t.text
                for char in my_str:
                   if char not in punctuations:
                       no_punct = no_punct + char
                link_tokens = nltk.tokenize.word_tokenize(no_punct)
                for token in link_tokens:
                    tokens.append(token)
            texts.append(tokens)
        except:
            pass

    for i in range(len(texts)):
        texts[i] = [t for t in texts[i] if len(t) == len(a)]
    for text in texts:
        for i in range(len(text)):
            text[i] = text[i].lower()

    for i in range(len(texts)):
        texts[i] = [t for t in texts[i] if t not in stopwords]

    for i in range(len(texts)):
        texts[i] = [t for t in texts[i] if not RepresentsInt(t)]

    if not IsAllZeros(a):
        for i in range(len(a)):
            if a[i] == '0':
                continue
            else:
                for j in range(len(texts)):
                    texts[j] = [t for t in texts[j] if t[i] == a[i]]


    counts = {}
    for text in texts:
        for t in text:
            if counts.get(t) == None:
                counts[t] = 1
            else:
                counts[t] += 1
    counts = sorted(counts.items(), key=operator.itemgetter(1))
    count = 1
    sum = 0
    answers = []
    occ = []
    for i in range(len(counts)):
        if count > 5:
            break
        sum += counts[-count][1]
        occ.append(counts[-count][1])
        answers.append(counts[-count][0])
        count += 1

    conf = []
    for o in occ:
        conf.append(o/sum)
    return answers, conf

questions = []
answers = []
with open('p.txt') as f:
    lines = f.readlines()
    for i in range(len(lines)):
        if i % 2 == 0:
            questions.append(lines[i])
        else:
            answers.append(lines[i])

for i in range(len(questions)):
    questions[i] = questions[i].replace('\n', '')

for i in range(len(answers)):
    answers[i] = answers[i].strip()


new_ans = []
confs = []
for i in range(len(questions)):
    a, c = get_ans(questions[i], answers[i])
    new_ans.append(a)
    confs.append(c)

w_lines = []

for i in range(len(new_ans)):
    s = ""
    for j in range(len(new_ans[i])):
        s = s + new_ans[i][j] + ' ' + str(confs[i][j]) + ' '
    w_lines.append(s)


with open('p_web.txt', 'w') as f:
    for line in w_lines:
        f.write(line)
        f.write('\n')
