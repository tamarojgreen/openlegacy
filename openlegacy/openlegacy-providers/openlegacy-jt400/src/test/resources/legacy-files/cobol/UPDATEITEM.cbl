1.00 130526        IDENTIFICATION DIVISION.                                                 
1.01 131028        DATA DIVISION.                                                           
2.00 140105        PROGRAM-ID. UPDATEITEM.                                                  
2.01 130609        LINKAGE SECTION.                                                         
2.02 131229           01 ITEM-NUM        PIC 9(8).                                          
2.04 131229           01 ITEM-RECORD.                                                       
2.05 131229             02 ITEM-NAME     PIC X(16).                                         
2.06 131229             02 DESCRIPTION   PIC X(28).                                         
2.07 131229             02 WEIGHT        PIC 9(4).                                          
2.08 131229           01 SHIPPING.                                                          
2.09 131229             02 METHOD        PIC X(10).                                         
2.10 131229             02 DAYS          PIC 9(4).                                          
3.00 131231        PROCEDURE DIVISION USING ITEM-NUM ITEM-RECORD SHIPPING.                  
3.01 131229        BEGIN.                                                                   
4.06 131010        HALT.                                                                    
