olApp.service('$olData', ['$http', function($http){
    var olDataInstance = {};
    
    olDataInstance.getItems = function(successCallback){        
        var url = "Items";
        console.log(url);
        this.RESTrequest(url,successCallback);
        //this.DebugRequest(url,successCallback);
    };
    
    olDataInstance.getItemDetails = function(itemId,successCallback){        
        var url = "ItemDetails/" + itemId;
        console.log(url);
        this.RESTrequest(url,successCallback);
    };
    
    olDataInstance.getWarehouses = function(successCallback){        
        var url = "Warehouses";
        console.log(url);
        this.RESTrequest(url,successCallback);
        //this.DebugRequest(url,successCallback);
    };
    
    olDataInstance.getWarehouseDetails = function(warehouseId,successCallback){        
        var url = "WarehouseDetails/" + warehouseId;
        console.log(url);
        this.RESTrequest(url,successCallback);
    };

    
    olDataInstance.RESTrequest = function(url, successCallback){
        console.log('url : ' + url);
        $http(
            {
                method : 'GET',
                data : '',
                url : olConfig.baseURL + '/' + url,
                headers : {
                    'Content-Type' : 'application/json',
                    'Accept' : 'application/json'
                }
            }
        )
        .success(function(data,status,headers,config){
        	successCallback(angular.fromJson(data))
        })
        .error(function(data, status, headers, config){
            alert(data)
        });
    };
    
    
    
    
    
    
    
    olDataInstance.getShippingList = function(successCallback){        
       
    	var result = {shippingList:[
    	               {name:"Jennifer R. Young", address:"3103 Byrd Lane Tijeras NM 87008", phone:"903-427-0380", date:"10/11/2012"},
    	               {name:"Arthur B. Craft", address:"2056 Florence Street Clarksville TX 75426", phone:"902-327-1340", date:"13/10/2012"},
    	               {name:"Brian K. Milliken", address:"2870 Mudlick Road Spokane WA 99201", phone:"509-481-5251", date:"12/03/2012"},
    	               {name:"John M. Popham", address:"4943 My Drive New York NY 10011", phone:"347-228-6752", date:"10/04/2013"}
    	             ]};
    	successCallback(result);
    };
    
     
    
    
    
    
    
    
    
    
    
   //    ******************************** start of debug methos ******************************** 
    olDataInstance.DebugRequest = function(url, successCallback){
        switch (url)
        {
            case "Items":
                result = '{"model":{"entity":{"positionTo":"","itemsRecords":[{"action_":null,"alphaSearch":"Water Ball","itemDescription":"Water Ball - Balls","itemNumber":3002},{"action_":null,"alphaSearch":"Frisbee","itemDescription":"Dog Frisbee - Pet Toys","itemNumber":3003},{"action_":null,"alphaSearch":"Pig Bank","itemDescription":"Pig Saving Bank - Ceramics","itemNumber":3004},{"action_":null,"alphaSearch":"Kid Guitar","itemDescription":"Kids Guitar - Musical Toys","itemNumber":3015},{"action_":null,"alphaSearch":"Poker","itemDescription":"Hot Poker Playing Cards - Cards","itemNumber":3024},{"action_":null,"alphaSearch":"Helicopter","itemDescription":"Toy Helicopter - Remote control toys","itemNumber":3028},{"action_":null,"alphaSearch":"Bumper Car","itemDescription":"Bumper Car - Electrical Toys","itemNumber":3030},{"action_":null,"alphaSearch":"Organ","itemDescription":"37 Key Electronic Organ - Plastic Toys","itemNumber":3041},{"action_":null,"alphaSearch":"Ball Pool","itemDescription":"Ball Pool - Novelty Toys","itemNumber":3050},{"action_":null,"alphaSearch":"Turtle","itemDescription":"Twilight Turtle - Musical Toys","itemNumber":3056},{"action_":null,"alphaSearch":"Toy Car","itemDescription":"Ride on Toy Car - Vehicle Toys","itemNumber":3063},{"action_":null,"alphaSearch":"Puzzle","itemDescription":"Wooden Puzzle - Wooden Toys","itemNumber":3065}],"focusField":null,"actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Exit","alias":"exit","actionName":"F3","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F3","macro":false}},{"displayName":"Create","alias":"create","actionName":"F6","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F6","macro":false}},{"displayName":"Cancel","alias":"cancel","actionName":"F12","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F12","macro":false}}]},"entityName":"Items","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"},{"entityName":"Items","displayName":"Items","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.Items"}],"actions":null}}';
                break;
            case "ItemDetails/3004":
                result = '{"model":{"entity":{"itemNumber":2000,"itemDescription":"Domino Cubes - Board Games","alphaSearch":"Domino Cub","supercedingItemfrom":"2210","supercedingItemto":"2215","substituteItemNumber":"","manufacturersItemNo":"","stockGroup":"StandardStockGroup","itemWeight":850,"palletLabelRequired":false,"itemDetails2":null,"focusField":"itemDescription","actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Prompt","alias":"prompt","actionName":"F4","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F4","macro":false}},{"displayName":"Cancel","alias":"cancel","actionName":"F12","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F12","macro":false}},{"displayName":"Delete","alias":"delete","actionName":"CombinedTerminalAction","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"SHIFT","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"additionalKey":"SHIFT","terminalAction":"org.openlegacy.terminal.actions.TerminalActions$F2","macro":false}}],"stockGroupDescription":"Standard stock group"},"entityName":"ItemDetails","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"},{"entityName":"Items","displayName":"Items","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.Items"},{"entityName":"ItemDetails","displayName":"Main Item Details","current":true,"requiresParameters":true,"entityClass":"org.openlegacy.terminal.samples.model.ItemDetails"}],"actions":null}}';
                break;
            case "Warehouses":
                result = '{"model":{"entity":{"positionTo":"","warehousesRecords":[{"action_":null,"whse":1,"description":"Magics warehouse"},{"action_":null,"whse":2,"description":"Baby Toys warehouse"},{"action_":null,"whse":3,"description":"Discount warehouse"},{"action_":null,"whse":4,"description":"Pet Toys warehouse"},{"action_":null,"whse":5,"description":"Kid Toys warehouse"},{"action_":null,"whse":6,"description":"Electronic Toys warehouse"},{"action_":null,"whse":7,"description":"Wooden Toys warehouse"},{"action_":null,"whse":9,"description":"Eastern warehouse"},{"action_":null,"whse":10,"description":"Western warehouse"},{"action_":null,"whse":11,"description":"Musical Toys warehouse"},{"action_":null,"whse":12,"description":"Sport equipment warehouse"},{"action_":null,"whse":13,"description":"Plastic Toys warehouse"}],"focusField":null,"actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Exit","alias":"exit","actionName":"F3","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F3","macro":false}},{"displayName":"Create","alias":"create","actionName":"F6","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F6","macro":false}},{"displayName":"Cancel","alias":"cancel","actionName":"F12","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F12","macro":false}}]},"entityName":"Warehouses","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"},{"entityName":"Warehouses","displayName":"Warehouses","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.Warehouses"}],"actions":null}}';
                break;
            case "WarehouseDetails/2":
                result = '{"model":{"entity":{"warehouseNumber":3,"warehouseDescription":"Discount warehouse","warehouseType":"GL","warehouseTypeName":"General warehouse","costingType":1,"replenishmentCycleFlag":"","address":"4309 S Morgan Street, Chicago, IL, 60609, United States","phone":"919.372.3412","email":"chicago.wh@legacytoys.com","amendedDate":1295758800000,"amendedBy":"TESTUSER","createdDate":"21/12/2002","createdBy":"Terry","focusField":"warehouseDescription","actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Delete","alias":"delete","actionName":"CombinedTerminalAction","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"SHIFT","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"additionalKey":"SHIFT","terminalAction":"org.openlegacy.terminal.actions.TerminalActions$F2","macro":false}}],"warehouseTypeValues":null},"entityName":"WarehouseDetails","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"},{"entityName":"Warehouses","displayName":"Warehouses","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.Warehouses"},{"entityName":"WarehouseDetails","displayName":"Warehouse Details","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.WarehouseDetails"}],"actions":null}}';
                break;
            case "login?user=user&password=password":
                result = 'OK';
                break;
            case "SignOn":
                result = '{"model":{"entity":{"system":"S44R5550","subsystem":"QBASE","display":"QPADEV00RT","user":"user1","password":"pwd1","programprocedure":"","menu":"","currentLibrary":"","errorMessage":"","focusField":"user","actions":[]},"entityName":"SignOn","paths":[{"entityName":"SignOn","displayName":"Sign On","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.SignOn"}],"actions":null}}';
                break;
            case "MainMenu":
                result = '{"model":{"entity":{"menuSelection":null,"company":101,"focusField":"menuSelection","actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Exit","alias":"exit","actionName":"F3","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F3","macro":false}},{"displayName":"Prompt","alias":"prompt","actionName":"F4","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F4","macro":false}},{"displayName":"Prev","alias":"prev","actionName":"F12","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F12","macro":false}},{"displayName":"Command","alias":"command","actionName":"CombinedTerminalAction","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"SHIFT","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"additionalKey":"SHIFT","terminalAction":"org.openlegacy.terminal.actions.TerminalActions$F9","macro":false}}]},"entityName":"MainMenu","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"}],"actions":null}}';
                break;
            case "DisplayProgramMessages":
                result = '{"model":{"entity":{"message":"Job 02345/TESTUSER/QPADEV00RT started on 02/09/11 at 11:44:08 in subsystem","focusField":null,"actions":[]},"entityName":"DisplayProgramMessages","paths":[{"entityName":"DisplayProgramMessages","displayName":"System Messages","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.DisplayProgramMessages"}],"actions":null}}';
                break;
            case "ItemDetails2/3004":
                result = '{"model":{"entity":{"itemNumberdesc":2000,"nlCostOfSalesAccount":"","nlSalesAccount":"","nlStockAccount":"","field2":"","field1":"","field":"","itemTypeBusinessArea":"","stockAnalysisCode":"","stockValueGroup":"","stockInventoryGroup":"","listPrice":null,"standardUnitCost":null,"stockInfo":{"createdDate":"17/01/2010","createdBy":"TESTUSER","amendedDate":"0/00/0000"},"focusField":"nlCostOfSalesAccount","actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Delete","alias":"delete","actionName":"CombinedTerminalAction","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"SHIFT","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"additionalKey":"SHIFT","terminalAction":"org.openlegacy.terminal.actions.TerminalActions$F2","macro":false}}]},"entityName":"ItemDetails2","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"},{"entityName":"Items","displayName":"Items","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.Items"},{"entityName":"ItemDetails","displayName":"Main Item Details","current":false,"requiresParameters":true,"entityClass":"org.openlegacy.terminal.samples.model.ItemDetails"},{"entityName":"ItemDetails2","displayName":"Item Stock details","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.ItemDetails2"}],"actions":null}}';
                break;
            case "InventoryMenu":
                result = '{"model":{"entity":{"menuSelection":null,"focusField":"menuSelection","actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Home","alias":"home","actionName":"F3","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F3","macro":false}},{"displayName":"Prev","alias":"prev","actionName":"F12","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F12","macro":false}},{"displayName":"Command","alias":"command","actionName":"CombinedTerminalAction","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"SHIFT","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"additionalKey":"SHIFT","terminalAction":"org.openlegacy.terminal.actions.TerminalActions$F9","macro":false}}]},"entityName":"InventoryMenu","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"}],"actions":null}}';
                break;
            case "WarehouseTypes":
                result = '{"model":{"entity":{"warehouseTypesRecords":[{"action_":null,"type":"DR","description":"Sports Toys warehouse"},{"action_":null,"type":"GL","description":"General warehouse"},{"action_":null,"type":"MT","description":"Games warehouse"},{"action_":null,"type":"VF","description":"Baby Toys warehouse"}],"focusField":null,"actions":[{"displayName":"Help","alias":"help","actionName":"F1","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F1","macro":false}},{"displayName":"Prompt","alias":"prompt","actionName":"F4","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F4","macro":false}},{"displayName":"Cancel","alias":"cancel","actionName":"F12","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"NONE","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"actionName":"F12","macro":false}},{"displayName":"Delete","alias":"delete","actionName":"CombinedTerminalAction","defaultAction":false,"targetEntityName":null,"global":true,"targetEntity":null,"targetEntityDefinition":null,"position":null,"additionalKey":"SHIFT","focusField":null,"type":"GENERAL","length":0,"when":".*","sleep":0,"action":{"additionalKey":"SHIFT","terminalAction":"org.openlegacy.terminal.actions.TerminalActions$F2","macro":false}}]},"entityName":"WarehouseTypes","paths":[{"entityName":"MainMenu","displayName":"Main Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.MainMenu"},{"entityName":"InventoryMenu","displayName":"Inventory Menu","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.InventoryMenu"},{"entityName":"Warehouses","displayName":"Warehouses","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.Warehouses"},{"entityName":"WarehouseDetails","displayName":"Warehouse Details","current":false,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.WarehouseDetails"},{"entityName":"WarehouseTypes","displayName":"Warehouse Types","current":true,"requiresParameters":false,"entityClass":"org.openlegacy.terminal.samples.model.WarehouseTypes"}],"actions":null}}';
                break;
            case "messages":
                result = '{"model":["Job 02345/TESTUSER/QPADEV00RT started on 02/09/11 at 11:44:08 in subsystem"]}';
                break;
            case "logoff":
                result = 'OK';
                break;
  
        }
        
        
         successCallback(result);
    }
    
  
//    ******************************** end of debug methos ********************************
    
    
    
    
    
    
    
    
    
    
    
    

    return olDataInstance;
}]);
       

