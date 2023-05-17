import spacy
import sys
from NLP import NLP
from Process import Process

#imports
#Main file, run this to run the program
#Project 2 Emerginf Technologies

nlp = spacy.load('en_core_web_sm')

process = Process()
lines_read_in = process.read_in_document()

allTokens = []
verbList = [('hundred','thousand','million','billion')]

for lines in lines_read_in:
    doc = nlp(lines)

    prev_token = doc[0]
    email = ""
    numbers = []
    noun = []

    for token in doc:
        tag = spacy.explain(token.tag_)
        if token.like_email:
            email = token.text
        if spacy.explain(token.tag_) == "cardinal number":
            numbers.append(token.text)
        if spacy.explain(prev_token.tag_)  == "conjunction, subordinating or preposition":
            if spacy.explain(token.tag_) == "noun, proper singular":
                noun.append(token.text)

        prev_token = token

    #If contains word
    tokens = NLP(email, process.SubsttituteWordForNum(numbers), noun)
    allTokens.append(tokens)


words = process.convertToFormat(allTokens)

for word in words:
    print(word)
