1.00 130526        IDENTIFICATION DIVISION.                                                 
2.00 131118        PROGRAM-ID. MIXED.                                                       
2.01 131118        DATA DIVISION.                                                           
2.02 130609        LINKAGE SECTION.                                                         
2.03 131117           01 VAR1 PIC X(10).                                                    
2.04 131117           01 STRUCT1.                                                           
2.05 131117             02 CHILD1 PIC X(10).                                                
2.06 131117             02 CHILD2 PIC X(10).                                                
2.07 131117           01 VAR2 PIC X(10).                                                    
2.08 131117           01 STRUCT2.                                                           
2.09 131117             02 CHILD3 PIC X(10).                                                
2.10 131117             02 CHILD4 PIC X(10).                                                
3.00 131117        PROCEDURE DIVISION USING VAR1 STRUCT1 VAR2 STRUCT2.                      
3.01 130609        BEGIN.                                                                   
3.03 131117            MOVE 'VAR1      '     TO VAR1.                                       
3.04 131118            MOVE 'VAR2      '     TO VAR2.                                       
3.05 131118            MOVE 'CHILD1    '     TO CHILD1.                                     
3.06 131118            MOVE 'CHILD2    '     TO CHILD2.                                     
3.07 131118            MOVE 'CHILD3    '     TO CHILD3.                                     
3.08 131118            MOVE 'CHILD4    '     TO CHILD4.                                     
4.03 131118                                                                                 
4.04 130609        HALT.                                                                    
