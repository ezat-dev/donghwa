package com.ace.donghwa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.domain.Recipe;
import com.ace.util.RecipeMap;

@Controller
public class HomeController {

	public static OpcUaClient client = null; 
	
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        return "recipeTest";
    }

    //OPC서버 연결시작
    public static void opcStart() throws UaException, InterruptedException, ExecutionException {
		client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");
			
		client.connect().get();
    }
    
    //OPC서버 연결종료
    public static void opcEnd() throws InterruptedException, ExecutionException {
		client.disconnect().get();
    }
 

    @RequestMapping(value = "/monitoring1", method = RequestMethod.GET)
    public String monitoring1(Locale locale, Model model) {
        return "monitoring1";
    }    
    
    @RequestMapping(value = "/monitoring1/view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> monitoring1View() throws UaException, InterruptedException, ExecutionException {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
	
    	if(client != null) {
	        // 여러 노드의 ID
	        List<NodeId> nodeIds = Arrays.asList(
	            new NodeId(2, "DONGHWA.PLC.M.M0100"), new NodeId(2, "DONGHWA.PLC.M.M0101"),
	            new NodeId(2, "DONGHWA.PLC.M.M0102"), new NodeId(2, "DONGHWA.PLC.M.M0103"),
	            new NodeId(2, "DONGHWA.PLC.M.M0104"), new NodeId(2, "DONGHWA.PLC.M.M0105"),
	            new NodeId(2, "DONGHWA.PLC.M.M0106"), new NodeId(2, "DONGHWA.PLC.M.M0107"),
	            new NodeId(2, "DONGHWA.PLC.M.M0108"), new NodeId(2, "DONGHWA.PLC.M.M0109"),
	            new NodeId(2, "DONGHWA.PLC.M.M0110"), new NodeId(2, "DONGHWA.PLC.M.M0111"),
	            new NodeId(2, "DONGHWA.PLC.M.M0112"), new NodeId(2, "DONGHWA.PLC.M.M0113"),
	            new NodeId(2, "DONGHWA.PLC.M.M0114"), new NodeId(2, "DONGHWA.PLC.M.M0115"),
	            new NodeId(2, "DONGHWA.PLC.M.M0116"), new NodeId(2, "DONGHWA.PLC.M.M0117"),
	            new NodeId(2, "DONGHWA.PLC.M.M0118"), new NodeId(2, "DONGHWA.PLC.M.M0119"),
	            new NodeId(2, "DONGHWA.PLC.M.M0120"), new NodeId(2, "DONGHWA.PLC.M.M0121"),
	            new NodeId(2, "DONGHWA.PLC.M.M0122"), new NodeId(2, "DONGHWA.PLC.M.M0123"),
	            new NodeId(2, "DONGHWA.PLC.M.M0124"), new NodeId(2, "DONGHWA.PLC.M.M0125"),
	            new NodeId(2, "DONGHWA.PLC.M.M0126"), new NodeId(2, "DONGHWA.PLC.M.M0127"),
	            new NodeId(2, "DONGHWA.PLC.M.M0128"), new NodeId(2, "DONGHWA.PLC.M.M0129"),
	            new NodeId(2, "DONGHWA.PLC.M.M0130"), new NodeId(2, "DONGHWA.PLC.M.M0131"),
	            new NodeId(2, "DONGHWA.PLC.M.M0132"), new NodeId(2, "DONGHWA.PLC.M.M0133"),
	            new NodeId(2, "DONGHWA.PLC.M.M0134"), new NodeId(2, "DONGHWA.PLC.M.M0135"),
	            new NodeId(2, "DONGHWA.PLC.M.M0136"), new NodeId(2, "DONGHWA.PLC.M.M0137"),
	            new NodeId(2, "DONGHWA.PLC.M.M0138"), new NodeId(2, "DONGHWA.PLC.M.M0139"),
	            new NodeId(2, "DONGHWA.PLC.M.M0140"), new NodeId(2, "DONGHWA.PLC.M.M0141"),
	            new NodeId(2, "DONGHWA.PLC.M.M0142"), new NodeId(2, "DONGHWA.PLC.M.M0143"),
	            new NodeId(2, "DONGHWA.PLC.M.M0144"), new NodeId(2, "DONGHWA.PLC.M.M0145"),
	            new NodeId(2, "DONGHWA.PLC.M.M0146"), new NodeId(2, "DONGHWA.PLC.M.M0147"),
	            new NodeId(2, "DONGHWA.PLC.M.M0148"), new NodeId(2, "DONGHWA.PLC.M.M0149"),
	            new NodeId(2, "DONGHWA.PLC.M.M0150"), new NodeId(2, "DONGHWA.PLC.M.M0151"),
	            new NodeId(2, "DONGHWA.PLC.M.M0152"), new NodeId(2, "DONGHWA.PLC.M.M0153"),
	            new NodeId(2, "DONGHWA.PLC.M.M0154"), new NodeId(2, "DONGHWA.PLC.M.M0155"),
	            new NodeId(2, "DONGHWA.PLC.M.M0156"), new NodeId(2, "DONGHWA.PLC.M.M0157"),
	            new NodeId(2, "DONGHWA.PLC.M.M0158"), new NodeId(2, "DONGHWA.PLC.M.M0159"),
	            new NodeId(2, "DONGHWA.PLC.M.M0160"), new NodeId(2, "DONGHWA.PLC.M.M0161"),
	            new NodeId(2, "DONGHWA.PLC.M.M0162"), new NodeId(2, "DONGHWA.PLC.M.M0163"),
	            new NodeId(2, "DONGHWA.PLC.M.M0164"), new NodeId(2, "DONGHWA.PLC.M.M0165"),
	            new NodeId(2, "DONGHWA.PLC.M.M0166"), new NodeId(2, "DONGHWA.PLC.M.M0167"),
	            new NodeId(2, "DONGHWA.PLC.M.M0168"), new NodeId(2, "DONGHWA.PLC.M.M0169"),
	            new NodeId(2, "DONGHWA.PLC.M.M0170"), new NodeId(2, "DONGHWA.PLC.M.M0171"),
	            new NodeId(2, "DONGHWA.PLC.M.M0172"), new NodeId(2, "DONGHWA.PLC.M.M0173"),
	            new NodeId(2, "DONGHWA.PLC.M.M0174"), new NodeId(2, "DONGHWA.PLC.M.M0175"),
	            new NodeId(2, "DONGHWA.PLC.M.M0176"), new NodeId(2, "DONGHWA.PLC.M.M0177"),
	            new NodeId(2, "DONGHWA.PLC.M.M0178"), new NodeId(2, "DONGHWA.PLC.M.M0179"),
	            new NodeId(2, "DONGHWA.PLC.M.M0180"), new NodeId(2, "DONGHWA.PLC.M.M0181"),
	            new NodeId(2, "DONGHWA.PLC.M.M0182"), new NodeId(2, "DONGHWA.PLC.M.M0183"),
	            new NodeId(2, "DONGHWA.PLC.M.M0184"), new NodeId(2, "DONGHWA.PLC.M.M0185"),
	            new NodeId(2, "DONGHWA.PLC.M.M0186"), new NodeId(2, "DONGHWA.PLC.M.M0187"),
	            new NodeId(2, "DONGHWA.PLC.M.M0188"), new NodeId(2, "DONGHWA.PLC.M.M0189"),
	            new NodeId(2, "DONGHWA.PLC.M.M0190"), new NodeId(2, "DONGHWA.PLC.M.M0191"),
	            new NodeId(2, "DONGHWA.PLC.M.M0192"), new NodeId(2, "DONGHWA.PLC.M.M0193"),
	            new NodeId(2, "DONGHWA.PLC.M.M0194"), new NodeId(2, "DONGHWA.PLC.M.M0195"),
	            new NodeId(2, "DONGHWA.PLC.M.M0196"), new NodeId(2, "DONGHWA.PLC.M.M0197"),
	            new NodeId(2, "DONGHWA.PLC.M.M0198"), new NodeId(2, "DONGHWA.PLC.M.M0199")
	            
	        );    	
	
	        // 여러 노드 읽기
	        List<CompletableFuture<DataValue>> futures = nodeIds.stream()
	            .map(nodeId -> client.readValue(0, TimestampsToReturn.Both, nodeId))
	            .collect(Collectors.toList());    	
	
	        // CompletableFuture를 사용하여 모든 값을 한꺼번에 대기
	        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	        allFutures.get();
	        
	        Map<String, Object> multiValues = new HashMap<>();
	        for (int i = 0; i < nodeIds.size(); i++) {
	            DataValue value = futures.get(i).get();
	            NodeId node = nodeIds.get(i);
	            multiValues.put(node.getIdentifier().toString().replace(".","_"), value.getValue().getValue());
	        }
	        returnMap.put("multiValues", multiValues);	        
    	}else {
    		returnMap.put("multiValues", "Fail");
    	}
    	
    	return returnMap;    	
    }
    
    @RequestMapping(value = "/opc", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> opc() throws UaException, InterruptedException, ExecutionException {

        OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");

        // 클라이언트 연결
        client.connect().get();

        // 단일 노드 읽기
        NodeId singleNodeId = new NodeId(2, "OPC_TEST.TEST_GROUP.VALUE1");
        CompletableFuture<DataValue> singleFuture = client.readValue(0, TimestampsToReturn.Both, singleNodeId);
        DataValue singleValue = singleFuture.get();
        System.out.println("Read value: " + singleValue.getValue());

        // 여러 노드의 ID
        List<NodeId> nodeIds = Arrays.asList(
            new NodeId(2, "OPC_TEST.PLC.D.D902"),
            new NodeId(2, "OPC_TEST.PLC.D.D903"),
            new NodeId(2, "OPC_TEST.PLC.D.D904")
        );

        // 여러 노드 읽기
        List<CompletableFuture<DataValue>> futures = nodeIds.stream()
            .map(nodeId -> client.readValue(0, TimestampsToReturn.Both, nodeId))
            .collect(Collectors.toList());

        // CompletableFuture를 사용하여 모든 값을 한꺼번에 대기
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();

        // 각 노드의 값을 출력 및 저장
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("singleValue", singleValue.getValue().getValue());
        Map<String, Object> multiValues = new HashMap<>();
        for (int i = 0; i < nodeIds.size(); i++) {
            DataValue value = futures.get(i).get();
            multiValues.put(nodeIds.get(i).toString(), value.getValue().getValue());
        }
        responseMap.put("multiValues", multiValues);
        System.out.println("읽어온 노드값 : " + multiValues);
        // 클라이언트 연결 종료
        client.disconnect().get();

        return responseMap;
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> write(
    		@RequestParam("nodeId") String nodeIdStr,@RequestParam("value") short valueStr,
    		@RequestParam("nodeId2") String nodeIdStr2,@RequestParam("value2") String valueStr2,
    		@RequestParam("nodeId3") String nodeIdStr3,@RequestParam("value3") boolean valueStr3
    		) 
    		throws UaException, InterruptedException, ExecutionException {

    	System.out.println("nodeIdStr : "+nodeIdStr+"// valueStr : "+valueStr);
    	
        OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");

        
        // 클라이언트 연결
        client.connect().get();

        UShort namespaceIndex = Unsigned.ushort(2);
        
        // 노드 ID 생성
        NodeId nodeId = new NodeId(namespaceIndex, nodeIdStr);

        // 노드 ID 생성
        NodeId nodeId_s = new NodeId(namespaceIndex, nodeIdStr2);
        
        // 노드 ID 생성
        NodeId nodeId_b = new NodeId(namespaceIndex, nodeIdStr3);
        
        DataValue dataValue = new DataValue(new Variant(valueStr));
        DataValue dataValue_s = new DataValue(new Variant(valueStr2));
        DataValue dataValue_b = new DataValue(new Variant(valueStr3));

        System.out.println("쓰는 dataValue 타입 : "+dataValue.getValue().getValue().getClass());        
        
        // 노드에 값 쓰기
        CompletableFuture<StatusCode> writeFuture = client.writeValue(nodeId, dataValue);
        CompletableFuture<StatusCode> writeFuture_s = client.writeValue(nodeId_s, dataValue_s);
        CompletableFuture<StatusCode> writeFuture_b = client.writeValue(nodeId_b, dataValue_b);
        
        
        StatusCode statusCode = writeFuture.get();

        // 값이 성공적으로 쓰여졌는지 확인
        if (statusCode.isGood()) {
            System.out.println("Value written successfully");
        } else {
            System.out.println("Failed to write value: " + statusCode);
        }
        
        StatusCode statusCode_s = writeFuture_s.get();
        
        // 값이 성공적으로 쓰여졌는지 확인
        if (statusCode_s.isGood()) {
        	System.out.println("Value written successfully_s");
        } else {
        	System.out.println("Failed to write value_s: " + statusCode_s);
        }
        
        StatusCode statusCode_b = writeFuture_b.get();
        
        // 값이 성공적으로 쓰여졌는지 확인
        if (statusCode_b.isGood()) {
        	System.out.println("Value written successfully_b");
        } else {
        	System.out.println("Failed to write value_b: " + statusCode_b);
        }
        
        
        // 클라이언트 연결 종료
        client.disconnect().get();
        
        // 응답 생성
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Written value: " + valueStr + " to node: " + nodeIdStr);

        return response;
    }
    
    @RequestMapping(value = "/write_test", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> write_test(Recipe recipe) 
    				throws UaException, InterruptedException, ExecutionException {
    	
        OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.63:5660");
        // 클라이언트 연결
        client.connect().get();
    	
    	RecipeMap recipeMap = new RecipeMap();
    	List<String> nodeList = new ArrayList<String>();
    	
    	
    	for(int i=0; i<3; i++) {
    		String process_code = recipeMap.rMapRtn("recipe.getProcess_step_"+i+"()");
    		String time = recipeMap.rMapRtn("recipe.getTime_"+i+"()");
    		String temperature = recipeMap.rMapRtn("recipe.getTemperature_"+i+"()");
//    		Map<String, Object> nodemap = new HashMap<String, Object>();
            
        	System.out.println(recipe.getProcess_step_0());
        	System.out.println(recipe.getTime_0());
        	System.out.println(recipe.getTemperature_0());
        	
        	
    		if("0".equals(process_code)) {
    			
    			nodeList.add(process_code);
    			nodeList.add(time);
    			nodeList.add(temperature);
    			
    			break;
    		}else {    			
    			nodeList.add(process_code);
    			nodeList.add(time);
    			nodeList.add(temperature);
    		}
    	}
    	
    	System.out.println("사이즈 : "+nodeList.size());
    	
/*
 
        // 여러 노드의 ID
        List<NodeId> nodeIds = Arrays.asList(
            new NodeId(2, "OPC_TEST.PLC.D.D902"),
            new NodeId(2, "OPC_TEST.PLC.D.D903"),
            new NodeId(2, "OPC_TEST.PLC.D.D904")
        );

        // 여러 노드 읽기
        List<CompletableFuture<DataValue>> futures = nodeIds.stream()
            .map(nodeId -> client.readValue(0, TimestampsToReturn.Both, nodeId))
            .collect(Collectors.toList());

        // CompletableFuture를 사용하여 모든 값을 한꺼번에 대기
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();
 */
    	
    	for(int j=0; j<nodeList.size(); j++) {
	    	String node1 = nodeList.get(j).toString();
	
	    	System.out.println(node1);
	    	
	    	
	        UShort namespaceIndex = Unsigned.ushort(2);
	        
	        // 노드 ID 생성
	        NodeId nodeId = new NodeId(namespaceIndex, node1);
	        DataValue dataValue = new DataValue(new Variant(recipe.getProcess_step_0()));
	        CompletableFuture<StatusCode> writeFuture = client.writeValue(nodeId, dataValue);
	        
	        StatusCode statusCode = writeFuture.get();
	
	        // 값이 성공적으로 쓰여졌는지 확인
	        if (statusCode.isGood()) {
	            System.out.println("Value written successfully");
	        } else {
	            System.out.println("Failed to write value: " + statusCode);
	        }        
    	}	        
    	// 응답 생성
    	Map<String, String> response = new HashMap<>();
    	response.put("status", "success");

    	return response;
    }
}

