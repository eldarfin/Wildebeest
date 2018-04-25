import requests
from lxml import html
from bs4 import BeautifulSoup
import nltk
import operator

#

nltk.download('punkt')
stopwords = ['a','about','above','across','after','afterwards','again','against','all','almost','alone','along','already','also','although','always','am','among','an','and','another','any','anyhow','anyone','anything','anyway','anywhere','are','around','as','at','back','be','became','because','become','becomes','becoming','been','before','behind','being','below','beside','besides','between','beyond','both','bottom','but','by','call','can','cannot','cant','co','con','could','couldnt','did','didnt','do','does','doesnt','done','dont','down','due','during','each','either','else','elsewhere','enough','even','ever','every','everyone','everything','everywhere','except','few','fill','for','former','formerly','from','front','full','further','get','give','go','had','has','hasnt','have','havent','he','hence','her','here','hereafter','hereby','herein','hereupon','hers','herself','him','himself','his','how','however','i','if','ill','im','in','indeed','into','is','isnt','it','its','itself','ive','just','last','least','less','may','me','meanwhile','might','mine','more','moreover','most','mostly','move','much','must','my','myself','name','namely','neither','never','nevertheless','next','no','nobody','none','nor','not','nothing','now','nowhere','of','off','often','on','once','one','only','onto','or','other','others','otherwise','our','ours','ourselves','out','over','own','part','per','perhaps','please','put','rather','same','see','seem','seemed','seeming','seems','serious','she','should','shouldnt','side','since','so','some','somehow','someone','something','sometime','sometimes','somewhere','still','such','than','that','the','their','them','themselves','then','thence','there','thereafter','thereby','therefore','therein','thereupon','these','they','theyre','this','those','though','three','through','throughout','thru','thus','to','together','too','top','toward','towards','twelve','under','until','up','upon','us','very','was','we','well','were','weve','what','whatever','when','whence','whenever','where','whereas','whereby','wherein','whereupon','wherever','whether','which','while','whither','who','whoever','whole','whom','whose','why','will','with','within','without','wont','would','wouldnt','yet','you','your','youre','yours','yourself','yourselves','youve']

url = "http://www.google.com/search?q=ballpoint+pen+brand+crossword"
r = requests.get(url)
soup = BeautifulSoup(r.text, 'html.parser')
links = []
for item in soup.find_all('h3', attrs={'class' : 'r'}):
    links.append(item.a['href'][7:])
texts = []

for link in links:
    try:
        page = requests.get(link)
        soup = BeautifulSoup(page.content, 'html.parser')
        for script in soup(["script", "style"]):
            script.extract()
        tokens = nltk.tokenize.word_tokenize(soup.get_text())
        tags = soup.find_all('a')
        for t in tags:
            link_tokens = nltk.tokenize.word_tokenize(t.text)
            for token in link_tokens:
                tokens.append(token)
        texts.append(tokens)
    except:
        pass

for i in range(len(texts)):
    texts[i] = [t for t in texts[i] if len(t) == 3]
for text in texts:
    for i in range(len(text)):
        text[i] = text[i].lower()

counts = {}
for text in texts:
    for t in text:
        if counts.get(t) == None:
            counts[t] = 1
        else:
            counts[t] += 1
counts = sorted(counts.items(), key=operator.itemgetter(1))
print(counts)
