# University Social Platform Java Program

## Overview
This Java program performs social network analysis using an adjacency list representation of a graph. It reads student information from a file, populates the graph, and offers various social network analysis functionalities.

## Contents
1. [Usage](#usage)
2. [Input File Format](#input-file-format)
3. [Program Features](#program-features)
4. [How to Run](#how-to-run)
5. [Sample Output](#sample-output)
6. [Contributing](#contributing)
7. [License](#license)

## Usage
The program provides social network analysis features such as removing friendships, deleting accounts, counting friends, finding friend circles, calculating closeness centrality, and identifying connectors.

## Input File Format
The input file should be a tab-separated text file containing information about students who have joined a university's social platform. Each line represents a student with various details, including ID, first name, last name, college, department, email, friend count, and friend IDs.

**Example input file format:**
```plaintext
id  studentsFirstName  studentsLastName  college  department  email  friendCount  friend1  friend2  friend3  friend4  friend5
1   John               Doe               Arts     History     john@example.com  3  2  5  7  9  12
2   Jane               Smith             Science  Physics     jane@example.com  2  1  8  10  15
...
