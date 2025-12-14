package co.parameta.technical.test.soap.endpoint;

import co.parameta.technical.test.commons.pojo.EmployeeResponsePojo;
import co.parameta.technical.test.commons.pojo.SaveEmployeeRequestPojo;
import co.parameta.technical.test.soap.service.ISaveEmployeeService;
import co.parameta.technical.test.soap.service.IViewAllUserInformationService;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeRequest;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static co.parameta.technical.test.soap.util.constant.Constant.NAMESPACE_URI;

@Endpoint
@RequiredArgsConstructor
public class EmployeeEndpoint {

    private final ISaveEmployeeService iSaveEmployeeService;
    private final IViewAllUserInformationService iViewAllUserInformationService;

    @PayloadRoot(
            namespace = NAMESPACE_URI,
            localPart = "SaveEmployeeRequest"
    )
    @ResponsePayload
    public EmployeeResponsePojo saveEmployee(
            @RequestPayload SaveEmployeeRequestPojo request
    ) {
        return iSaveEmployeeService.saveEmployee(request.getEmployee());
    }

    @PayloadRoot(
            namespace = NAMESPACE_URI,
            localPart = "GetAllInformationEmployeeRequest"
    )
    @ResponsePayload
    public GetAllInformationEmployeeResponse getAllEmployeeInformation(
            @RequestPayload GetAllInformationEmployeeRequest request
    ) {
        return iViewAllUserInformationService.allInformationEmployee(request);
    }


}
