
Grace Zheng

For this project, I have 3 classes. The URCalculator contains the majority of my
methods. The main method is in the URCalculator class and it asks the user for an 
input. It also does everything that changes multiple ++ and --s as well as puts
everything into the right order of precedence. You can not only press enter without
entering any values (you can't just enter whitespace) You can not enter 
a variable without assigning it to a value. You also can't do 3 --4 5 if there 
is not a space between the negatives.The evalatePostFix class does the actual math
for everything. 

Additionally:
 - the user is able to show a specific variable by typing show var, just as they
 would for clearing a specific variable
 - i added a final double for pi, so if the user wanted to find out the value of pi,
 they could just type in show pi. However, they are unable to change the value of it 
 because i set it as a final double.
 - I did the same thing for pi as I did for e.
 - The user is able to find the max and the min values in the hashmap by typing show
 max or show min.
