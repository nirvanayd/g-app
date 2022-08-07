package com.nelly.application.service.app;

import com.nelly.application.domain.Agreements;
import com.nelly.application.dto.response.AgreementResponse;
import com.nelly.application.dto.response.AppInitDataResponse;
import com.nelly.application.enums.MarketingType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.service.AppDomainService;
import dto.EnumStringCodeValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppService {

    private final AppDomainService appDomainService;
    private final ModelMapper modelMapper;

    public AppInitDataResponse getAppInitData(String version) {
        List<Agreements> agreementsList = appDomainService.selectAgreements(version);
        List<EnumStringCodeValue> styleTypeList = StyleType.getStyleList();
        List<EnumStringCodeValue> marketingTypeList = MarketingType.getMarketingTypeList();

        List<AgreementResponse> agreementResponses = agreementsList.stream().
                map(u -> modelMapper.map(u, AgreementResponse.class)).collect(Collectors.toList());

        Optional<AgreementResponse> marketingOptional =
                agreementResponses.stream().filter(a -> a.getAgreementTypeCode().equals("marketing")).findFirst();

        marketingOptional.ifPresent(agreementResponse -> agreementResponse.setItemList(marketingTypeList));

        AppInitDataResponse response = new AppInitDataResponse();

        response.setAgreementsList(agreementResponses);
        response.setStyleList(styleTypeList);
//        response.setMarketingTypeList(marketingTypeList);
        return response;
    }
}
