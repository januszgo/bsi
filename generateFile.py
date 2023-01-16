name = "/Users/godle/Documents/Kod/BSI/bsi/string10-9.txt"
f = open(name, "wb")
f.seek(10**9)
f.write(b"a")
f.close()
import os
os.stat(name).st_size