0001.00        IDENTIFICATION DIVISION.          
0002.00        PROGRAM-ID. BOOLTEST.             
0002.01        DATA DIVISION.                    
0002.03        LINKAGE SECTION.                  
0002.04          01 BOOLPART.                    
0002.05            02 BOOL PIC X.                
0003.00        PROCEDURE DIVISION USING BOOLPART.
0003.01        BEGIN.                            
0003.02            EVALUATE BOOL                 
0003.03            WHEN 'Y'                      
0003.04               MOVE 'N' TO BOOL           
0003.05            WHEN 'N'                      
0003.06               MOVE 'Y' TO BOOL           
0003.07            END-EVALUATE.                 
0006.00        HALT.                             
       