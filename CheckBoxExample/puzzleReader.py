def puzzleReader(filename):
	file = open(filename,"r")
	puzzles = np.empty( 6, dtype=object)
	puzzles[:] = puzzle_object()
	index = 0
	for line in file:
		questions = np.empty((2, 5),dtype=object)
		questions[:]=" "
		chars =  np.empty((5, 5),dtype=object)
		chars[:] = ' '
		answers = np.empty((5, 5),dtype=object)
		answers[:] = ' '
		arr = line.split(" ")
		ID = int(arr[0])
		fills = arr[1]
		fills = fills.split(",")
		fills[0] = fills[0][1:]
		fills[len(fills)-1] = fills[len(fills)-1][:len(fills[len(fills)-1])-1]
		for a in fills:
			num = int(a)
			w = num/5
			h = num%5
			chars[w][h]=None
			answers[w][h]=None
		line = line[(line.find("}")+2):]
		q = line.split(",,")
		for i in range(10):
			questions[i/5][i%5]=q[i]
		answerChars = q[10]
		j=0
		for i in range(len(answerChars)):
			if(answers[i/5][i%5] is None):
				j=j-1
			else:
				answers[i/5][i%5]=answerChars[j]
			j=j+1
		obj = puzzle_object(ID,questions,chars,answers)
		puzzles[index] = obj 
		index=index+1
	return puzzles	