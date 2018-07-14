import requests

base_url = 'http://localhost:8080'

def add_word(word):    
    r = requests.post(base_url + "/insert/" + word)
    print(r.json())

def get_top_words(word):  
    r = requests.get(base_url + "/match/" + word)
    print(r.json())

add_word("aaa")
add_word("bbb")
add_word("zvx")
add_word("zzz")

get_top_words("ghj")

