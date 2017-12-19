solve(Node, Solution) :-
	dfs([], Node, Solution).

dfs(Path, Node, [Node|Path]) :- 
	goal(Node).

dfs(Path, Node, Sol) :-
	c(Node, Node1),
	\+ member(Node1, Path),
	dfs([Node|Path], Node1, Sol).
