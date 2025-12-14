package co.parameta.technical.test.soap.endpoint;

import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.EmployeeResponsePojo;
import co.parameta.technical.test.commons.pojo.EmployeeResponseTypePojo;
import co.parameta.technical.test.commons.pojo.SaveEmployeeRequestPojo;
import co.parameta.technical.test.soap.service.ISaveEmployeeService;
import co.parameta.technical.test.soap.service.IViewAllUserInformationService;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeRequest;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeEndpointTest {

    @InjectMocks
    private EmployeeEndpoint endpoint;

    @Mock
    private ISaveEmployeeService saveEmployeeService;

    @Mock
    private IViewAllUserInformationService viewAllUserInformationService;

    @Test
    void saveEmployeeDelegatesToServiceAndReturnsResponse() {
        SaveEmployeeRequestPojo request = new SaveEmployeeRequestPojo();
        EmployeePojo employeePojo = new EmployeePojo();
        request.setEmployee(employeePojo);

        EmployeeResponsePojo expected = new EmployeeResponsePojo();
        EmployeeResponseTypePojo responseType = new EmployeeResponseTypePojo();
        responseType.setStatus("200");
        responseType.setMessage("OK");
        expected.setResponse(responseType);

        when(saveEmployeeService.saveEmployee(employeePojo))
                .thenReturn(expected);

        EmployeeResponsePojo result = endpoint.saveEmployee(request);

        assertNotNull(result);
        assertSame(expected, result);

        verify(saveEmployeeService, times(1))
                .saveEmployee(employeePojo);
        verifyNoMoreInteractions(saveEmployeeService);
        verifyNoInteractions(viewAllUserInformationService);
    }

    @Test
    void getAllEmployeeInformationDelegatesToServiceAndReturnsResponse() {
        GetAllInformationEmployeeRequest request =
                new GetAllInformationEmployeeRequest();
        request.setIdEmployee(10);
        request.setTypeDocument("CC");
        request.setNumberDocument("123");

        GetAllInformationEmployeeResponse expected =
                new GetAllInformationEmployeeResponse();

        when(viewAllUserInformationService.allInformationEmployee(request))
                .thenReturn(expected);

        GetAllInformationEmployeeResponse result =
                endpoint.getAllEmployeeInformation(request);

        assertNotNull(result);
        assertSame(expected, result);

        verify(viewAllUserInformationService, times(1))
                .allInformationEmployee(request);
        verifyNoMoreInteractions(viewAllUserInformationService);
        verifyNoInteractions(saveEmployeeService);
    }
}
