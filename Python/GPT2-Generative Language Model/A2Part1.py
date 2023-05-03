from sentence_transformers import SentenceTransformer, util
import numpy as np
#from np import dot
from math import sqrt
import json

def get_tweets():
    tweets = []
    f = open('tweets-utf-8.json',"r",encoding='utf-8')
    lines = f.readlines()
    
    for x in lines:
        json_object = json.loads(x)
        tweets.append(json_object["text"])

    return tweets

def sort_by_sim(query_embedding,document_embeddings,documents):
    #scores = util.dot_score(query_embedding, document_embeddings)[0].cpu().tolist()
    # scores = np.dot(query_embedding.reshape(1,-1),document_embeddings)/(np.norm(query_embedding)*np.norm(document_embeddings, axis=1))
    scores = np.dot(document_embeddings, query_embedding)
    scores /= np.linalg.norm(document_embeddings, axis=1)
    scores /= np.linalg.norm(query_embedding)
    scores = np.nan_to_num(scores)
    score_documents = list(zip(scores, documents))
    doc_score_pairs = sorted(score_documents, key=lambda x: x[0], reverse=True)
    return doc_score_pairs

    
def glove_top25(query,documents):
    model = SentenceTransformer('sentence-transformers/average_word_embeddings_glove.840B.300d')
    query_embeddings = model.encode(query)
    document_embeddings = model.encode(documents)
    sorted = sort_by_sim(query_embeddings, document_embeddings, documents)
    sorted_25 = sorted[:25]
    return sorted_25

def minilm_top25(query,documents):
    model = SentenceTransformer('sentence-transformers/all-MiniLM-L6-v2')
    query_embeddings = model.encode(query)
    document_embeddings = model.encode(documents)
    sorted = sort_by_sim(query_embeddings, document_embeddings, documents)
    sorted_25 = sorted[:25]
    return sorted_25
        
## Test Code

tweets = get_tweets()

print("**************GLOVE*****************")
for p in glove_top25("I am looking for a job.",tweets): print(p)

print("**************MINILM*****************")
for p in minilm_top25("I am looking for a job.",tweets): print(p)