import numpy as np
class puzzle_object:
	questions = np.chararray((2, 5))
	questions[:]=" "
	chars = np.chararray((5, 5))
	chars[:] = ' '
	answers = np.chararray((5, 5))
	answers[:] = ' '
	def _init__(self,questions,chars,answers):
		self.questions=questions
		self.chars=chars
		self.answers=answers
	def check(self):
		return np.array_equal(chars,answers)
	def fill(self, chr, x, y):
		chars[x][y]= chr
	def reveal(self):
		chars = answers
