Rohit Kapoor
kapoor.83@osu.edu

The LISP Interpreter project is made in Java and contains the following files:

1) Token.java - This file contains the Token class, Tokens in my project are generated while Scanning a line and if the line is successfully scanned, the Tokens are used to build the SExpression tree later on.

2) SExpression.java - This file contains the class for an S Expression which can has a variable called child that can either be a Token or a Compound S Expression (CSExpression).

3) CSExpression.java - This file contains the class for a Compound S Expression, which has two variables 'left' and 'right', which are both SExpression.

4) ScannerParse.java - This file contains the MAIN FUNCTION and should be run in order to start the interpreter. The main function calls Scanner, Parser and Interpreter in order.

5) MyInterpreter.java - This file contains the main Interpreter which is called from ScannerParse.java after the scanning and parsing is complete. The function inside this file takes an SExpression evaluates it and returns an SExpression and also updates the D List.

Design Pattern:

My Interpreter reads input from Standard input and then scans the whole input in one go completely and if any errors are present it will display the error and stop executing further and exits. Scanning generates Tokens, which are stored in a List to be used later on.

If input is a file, the file is scanned completely at once and all the Tokens are generated. 

If the Scanning is successful (i.e. No errors are thrown), then Parsing is done which validates the Tokens against the specified grammar and also builds the SExpression Tree side by side. This phase also throws errors if detected, otherwise results in a successful creation of an SExpression.
This phase is also done at one go and all the SExpressions are generated from the Tokens at once.

If the above phase doesn’t throw any error and generates at least one SExpression, then that SExpression is passed to MyInterpreter, which interprets the SExpression according to the rules presented in the class and project description given to us.

If there are runtime errors, MyInterpreter throws them and stops executing further and program exits.

If there are no errors and MyInterpreter returns an SExpression, it is then passed to a function which prints the output using the pattern specified in the Project Description.

NOTE: As only one file is executed at a time, so if a new function is defined it should be defined before usage in the same file, as the interpreter exits after execution of one file.