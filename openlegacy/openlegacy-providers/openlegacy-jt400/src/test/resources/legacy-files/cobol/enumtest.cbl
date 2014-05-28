  IDENTIFICATION DIVISION.               
  PROGRAM-ID. ENUMTEST.                  
  DATA DIVISION.                         
  LINKAGE SECTION.                       
    01 EN1 PIC X(20).                    
    01 EN2 PIC X(20).                    
  PROCEDURE DIVISION USING EN1 EN2.      
  BEGIN.                                 
         MOVE 'THE RED COLOR' TO EN2.    
  HALT.                                  
