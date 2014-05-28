
  IDENTIFICATION DIVISION.             
  PROGRAM-ID. BIGINTCBL.                 
  LINKAGE SECTION.                     
     01 CHILD1 PIC 9(12).              
  PROCEDURE DIVISION USING CHILD1.     
  BEGIN.                               
      COMPUTE CHILD1 = CHILD1 + 1.     
      STOP RUN.                        
  HALT.                                
