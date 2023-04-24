import os
import sys
import time
import shutil
import random
import datetime
import requests
import speech_recognition as sr
from playsound import playsound
import pyaudio
import wave
import webbrowser
import urllib.request as urllib2
import json
from gtts import gTTS
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

# Define the permission requests
PERMISSIONS = [
    'RECORD_AUDIO',
    'ACCESS_COARSE_LOCATION',
    'ACCESS_FINE_LOCATION',
    'BLUETOOTH',
    'READ_EXTERNAL_STORAGE',
    'WRITE_EXTERNAL_STORAGE'
]

# Check if the permissions are granted or not
def has_permissions():
    for permission in PERMISSIONS:
        if not os.access(permission, os.X_OK):
            return False
    return True

# Request permissions if not granted
def request_permissions():
    for permission in PERMISSIONS:
        while not os.access(permission, os.X_OK):
            try:
                os.system('clear')
                print('Please grant the following permission to run the program:', permission)
                time.sleep(3)
                os.system('xdg-open "https://www.google.com/search?q={}"'.format(permission))
                time.sleep(5)
            except:
                print('Could not request permission', permission)
                time.sleep(3)

# Check if the program has the required permissions
if not has_permissions():
    request_permissions()

# Main program starts here
def speak(text):
    tts = gTTS(text=text, lang='en')
    filename = 'voice.mp3'
    tts.save(filename)
    playsound(filename)
    os.remove(filename)

def get_audio():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        audio = r.listen(source)
        said = ""
        try:
            said = r.recognize_google(audio)
            print(said)
        except Exception as e:
            print("Exception:", str(e))
    return said.lower()

def send_text(text):
    url = 'https://www.google.com/search?q=' + text
    webbrowser.get().open(url)
    speak("Here is what I found for" + text)

def open_application(text):
    if "chrome" in text:
        speak("Google Chrome")
        os.system("google-chrome")
    elif "firefox" in text or "mozilla" in text:
        speak("Opening Mozilla Firefox")
        os.system("firefox")
    elif "code" in text:
        speak("Opening Visual Studio Code")
        os.system("code")
    elif "notepad" in text:
        speak("Opening Notepad")
        os.system("notepad")
    elif "calculator" in text:
        speak("Opening Calculator")
        os.system("calc")
    else:
        speak("Application not available")

def process_text(input):
    try:
        if "what" in input or "who" in input:
            res = client.query(input)
            output = next(res.results).text
            speak(output)
        elif "search" in input:
            send_text(input)
        elif "find" in input:
            send_text(input)
        elif "where" in input:
            send_text(input)
        elif "open" in input:
            open_application(input)
        else:
            speak("I did not understand what you said")
    except:
        speak("I did not understand what you said")

# Request user input
input = get_audio()

# Process user input
process_text(input)
