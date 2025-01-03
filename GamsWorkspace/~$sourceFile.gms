Sets
    m   / m1*m3 /
    a   / a1*a5 /;

Parameter
    priority(a);
    

priority("a1") = 2;
priority("a2") = 2;
priority("a3") = 1;
priority("a4") = 1;
priority("a5") = 3;    

Variables
    obj;

Binary Variables
    x(m,a);
    
Equations 
    mip;

*Zielfunktion    
mip .. obj =e= sum((m,a), (1/priority(a))*x(m,a));

Equations
    nb1;

*Nebenbedingungen
nb1(a) .. sum(m, x(m,a)) =l= 1;

*Solve
option mip = CPLEX;
Model optModel /all/;
Solve optModel using mip maximize obj;


Display x.l;