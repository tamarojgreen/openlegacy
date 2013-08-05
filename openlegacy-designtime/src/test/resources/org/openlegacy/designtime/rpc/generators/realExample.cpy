01 XXOLAKKR-PARMS.                                        
   03 LK-CUSTOMER.                                        
       05 LK-CUST-IND           PIC X.                    
       05 LK-CUST-ID            PIC 9(9).                 
       05 LK-CUST-NAME          PIC X(25).                
       05 LK-CUST-ADDRESS       PIC X(25).                
       05 LK-CUST-PHONE         PIC X(20).                
       05 LK-CARDS OCCURS 10 TIMES INDEXED BY CARD-INX.   
          07 LK-CARD-IND            PIC X.                
          07 LK-CARD-NUMBER         PIC 9(11).            
          07 LK-CARD-PREFIX         PIC 9(5).             
          07 LK-CARD-SUG-SHERUT     PIC 9(3). 