import os
from random import randrange

# pip install pydub
from pydub import AudioSegment

music_folder = '/Users/kaytonrex/Music/Music/Python Music'

for filename in os.listdir(music_folder):
    if not filename.startswith("MP3-"):
        new_filename = str(randrange(100000000)) + ".mp3"
        
        old_file_path = os.path.join(music_folder, filename)
        new_file_path = os.path.join(music_folder, new_filename)
        
        os.rename(old_file_path, new_file_path)

music_files = []
for filename in os.listdir(music_folder):
    if not filename.startswith("MP3-"):
        music_files.append(filename)

music_files.sort()

string = "cat "

for filename in music_files:
    if not filename.startswith("MP3-"):
        string += filename + " "

string = string + " > MP3-6.mp3"
print(string)