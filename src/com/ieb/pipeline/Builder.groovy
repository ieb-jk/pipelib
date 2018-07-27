package com.ieb.pipeline;

node('Worker1') { 
    build() 
}

def build() {
    echo "We are here"
}