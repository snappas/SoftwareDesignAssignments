Assign2: Due 11:59PM July 11

A publisher is interested in collecting the number of reviews for their books from Amazon. 

The ISBN number of their books are present in a file, in sorted order of the number. Write a program that reads that file, goes out to Amazon (http://www.amazon.com/exec/obidos/ASIN/ISBN — replace ISBN with the actual ISBN) to get the title and number of reviews. 

The program then lists, in sorted order of the title, the title and the number of reviews. Finally it also prints the total number of reviews for all their books. 

It's possible that either an ISBN number is wrong or there was a failure in fetching the data. For each ISBN number for which the program failed to get the data, it should print the number along with a short error message that tells what went wrong.

Start really early, move forward incrementally with the help of feedback loops.
Don't bring in dependencies until you no longer can avoid it (last responsible
moment). Start with a few tests, minimum code to make those work. Bring forward
practices you learned from assignment 1.

Total [100]: 100
Program works [20]:
All tests pass [20]:
Quality of tests [20]:
Code coverage [10]:
Design quality [20]:
Quality of code [10]:
