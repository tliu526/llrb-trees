#!/usr/bin/env python3
from sys import stdin,stderr
lines=[]
comment = False
WHITE=(255,255,255)
RED=(255,0,0)
BLUE=(0,0,255)
BLACK=(0,0,0)
codeCount = 0
for line in stdin:
    line.strip()
    l = len(line)
    result=[]
    lineComment = False
    uncomment = False
    sawCode = False
    for i in range(l):
        if i < l-1:
            if line[i:i+2] == '//':
                comment = True
                lineComment = True
            elif line[i:i+2] == '/*':
                comment = True
            elif line[i:i+2] == '*/':
                uncomment = True
        if line[i] <= ' ':
            result.append(WHITE)
        elif comment:
            result.append(BLACK)
        else:
            if line[i] not in '{}':
                sawCode = True
            result.append(RED)
        if uncomment:
            comment = False
            uncomment = False
    if sawCode:
        codeCount += 1
    if lineComment:
        comment = False
    lines.append(result)
width = max(len(line) for line in lines)
print("P3")
print("{} {}".format(width,len(lines)))
print("255")
for line in lines:
    for i in range(width):
        (r,g,b) = line[i] if i < len(line) else WHITE
        print("{} {} {}".format(r,g,b),end=" ")
    print()
print("Saw {} lines of code.".format(codeCount),file=stderr)
