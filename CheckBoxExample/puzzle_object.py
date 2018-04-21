import numpy as np
class puzzle_object:
	ID = -1
	questions = np.empty((2, 5),dtype=object)
	questions[:]=" "
	chars = np.empty((5, 5),dtype=object)
	chars[:] = '.'
	answers = np.empty((5, 5),dtype=object)
	answers[:] = '.'
	def __init__(self,ID=-1,questions=None,chars=None,answers=None):
		self.ID = ID
		self.questions=questions
		self.chars=chars
		self.answers=answers
	def check(self):
		return np.array_equal(chars,answers)
	def fill(self, chr, x, y):
		chars[x][y]= chr
	def reveal(self):
		chars = answers
	def __repr__(self):
		return str("The ID is " +str(self.ID) + " first question is:\n"+ self.questions[0]+ "\nfirst filled answer is: "+ self.answers[0])
