     IDENTIFICATION DIVISION.             
     PROGRAM-ID. BOOLTEST.                
     DATA DIVISION.                       
     LINKAGE SECTION.                     
       01 BOOLPART.                       
         02 BOOL PIC X.                   
     PROCEDURE DIVISION USING BOOLPART.   
     BEGIN.                               
         EVALUATE BOOL                    
         WHEN 'Y'                         
            MOVE 'N' TO BOOL              
         WHEN 'N'                         
            MOVE 'Y' TO BOOL              
         END-EVALUATE.                    
     HALT.                                