package software.sigma.bu003.internship.coparts.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import software.sigma.bu003.internship.coparts.entity.PageParts;
import software.sigma.bu003.internship.coparts.entity.Part;
import software.sigma.bu003.internship.coparts.service.PartService;
import software.sigma.bu003.internship.coparts.service.exception.PartAlreadyCreatedException;
import software.sigma.bu003.internship.coparts.service.exception.PartNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PartController.class})
@AutoConfigureMockMvc(addFilters = false)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartService partService;

    private final String URL_TEMPLATE = "/parts";
    private final String URL_TEMPLATE_WITH_BRAND_CODE = "/parts/{brand}/{code}";
    private final String BRAND = "Audi";
    private final String CODE ="vw12345";

    private final Part testPart = new Part(BRAND, CODE);
    private final String testPartJSON = """
            {
                     "brand": "Audi",
                     "code": "vw12345"
            }
            """;

    private final String testWrongJSON = """
            {
                     "code": "vw12345"
            }
            """;

    @Test
    void shouldCreatePartSuccessfully() throws Exception {
        when(partService.createPart(testPart)).thenReturn(testPart);

        mockMvc.perform(post(URL_TEMPLATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(testPartJSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(testPartJSON));

        verify(partService).createPart(testPart);
    }

    @Test
    void shouldIsBadRequestIfBrandOrCodeIsNull() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testWrongJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendIsAcceptedIfPartAlreadyCreated() throws Exception {
        when(partService.createPart(testPart)).thenThrow(PartAlreadyCreatedException.class);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPartJSON))
                .andExpect(status().isAccepted());

        verify(partService).createPart(testPart);
    }

    @Test
    void shouldReturnAllPartsSuccessfully() throws Exception {
        List<Part> parts = new ArrayList<>();
        Page<Part> pageParts = new PageImpl<>(parts);
        PageParts expectedResponse = new PageParts(pageParts);

        when(partService.getAllParts(0, 5)).thenReturn(expectedResponse);

        mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(partService).getAllParts(0,5);
    }

    @Test
    void shouldReturnPartByBrandAndCodeSuccessfully() throws Exception {
        when(partService.getPart(BRAND, CODE)).thenReturn(testPart);

        mockMvc.perform(get(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE))
                .andExpect(status().isOk())
                .andExpect(content().json(testPartJSON));

        verify(partService).getPart(BRAND, CODE);
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        when(partService.getPart(BRAND, CODE)).thenThrow(PartNotFoundException.class);

        mockMvc.perform(get(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE))
                .andExpect(status().isNotFound());

        verify(partService).getPart(BRAND, CODE);
    }

    @Test
    void shouldUpdateSuccessfully() throws Exception {
        when(partService.updatePart(testPart)).thenReturn(testPart);

        mockMvc.perform(put(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPartJSON))
                .andExpect(status().isOk())
                .andExpect(content().json(testPartJSON));

        verify(partService).updatePart(testPart);
    }

    @Test
    void shouldIsBadRequestIfPutWithBrandOrCodeIsNull() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testWrongJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendIsNotFoundIfPartIsMissing() throws Exception {
        when(partService.updatePart(testPart)).thenThrow(PartNotFoundException.class);

        mockMvc.perform(put(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPartJSON))
                .andExpect(status().isNotFound());

        verify(partService).updatePart(testPart);
    }

    @Test
    void shouldSendIsOkIfDeleteSuccessfully() throws Exception {
        doNothing().when(partService).deletePart(BRAND, CODE);

        mockMvc.perform(delete(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE))
                .andExpect(status().isOk());

        verify(partService).deletePart(BRAND, CODE);
    }

    @Test
    void shouldSendISNotFoundIfPartIsMissing() throws Exception {
        doThrow(PartNotFoundException.class).when(partService).deletePart(BRAND, CODE);

        mockMvc.perform(delete(URL_TEMPLATE_WITH_BRAND_CODE, BRAND, CODE))
                .andExpect(status().isNotFound());

        verify(partService).deletePart(BRAND, CODE);
    }

    @Test
    void shouldReturnListOfPartsIfSuccessfully() throws Exception {
        List<Part> expectedList = List.of(testPart);

        when(partService.synchronizeWithTechnomir()).thenReturn(expectedList);

        mockMvc.perform(get(URL_TEMPLATE + "/synchronization"))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("[ %s ]", testPartJSON)));

        verify(partService).synchronizeWithTechnomir();

    }
}
