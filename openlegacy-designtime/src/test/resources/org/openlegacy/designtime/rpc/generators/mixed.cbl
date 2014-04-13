      IDENTIFICATION DIVISION.
      PROGRAM-ID. MIXED.                                                       
      DATA DIVISION.                                                           
      LINKAGE SECTION.                                                         
         01 VAR1 PIC X(10).                                                    
         01 STRUCT1.                                                           
            02 CHILD1 PIC X(10).                                                
            02 CHILD2 PIC X(10).                                                
         01 VAR2 PIC X(10).                                                    
         01 STRUCT2.                                                           
           02 CHILD3 PIC X(10).                                                
           02 CHILD4 PIC X(10).                                                
      PROCEDURE DIVISION USING VAR1 STRUCT1 VAR2 STRUCT2.                      
      BEGIN.                                                                   
          MOVE 'VAR1      '     TO VAR1.                                       
          MOVE 'VAR2      '     TO VAR2.                                       
          MOVE 'CHILD1    '     TO CHILD1.                                     
          MOVE 'CHILD2    '     TO CHILD2.                                     
          MOVE 'CHILD3    '     TO CHILD3.                                     
          MOVE 'CHILD4    '     TO CHILD4.                                     
                                                                               
      HALT.                                          