package com.nelly.application.service.app;

import com.nelly.application.domain.Agreements;
import com.nelly.application.domain.Brands;
import com.nelly.application.domain.UserAgreements;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.MarketingType;
import com.nelly.application.enums.PlaceType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.service.AppDomainService;
import com.nelly.application.service.BrandDomainService;
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
    private final BrandDomainService brandDomainService;
    private final ModelMapper modelMapper;

    public AppInitDataResponse getAppInitData(String version) {
        List<Agreements> agreementsList = appDomainService.selectAgreements(version);
        List<EnumStringCodeValue> styleTypeList = StyleType.getStyleList();
        List<EnumStringCodeValue> marketingTypeList = MarketingType.getMarketingTypeList();
        List<EnumStringCodeValue> ageList = AgeType.getAgeList();
        List<EnumStringCodeValue> placeList = PlaceType.getPlaceList();

        List<AgreementResponse> agreementResponses = agreementsList.stream().
                map(u -> modelMapper.map(u, AgreementResponse.class)).collect(Collectors.toList());

        Optional<AgreementResponse> marketingOptional =
                agreementResponses.stream().filter(a -> a.getAgreementTypeCode().equals("marketing")).findFirst();
        marketingOptional.ifPresent(agreementResponse -> agreementResponse.setItemList(marketingTypeList));

        List<Brands> brandList = brandDomainService.selectAppBrandList();
        List<BrandInitResponse> brandResponseList =
                brandList.stream().map(u -> modelMapper.map(u, BrandInitResponse.class)).collect(Collectors.toList());

        AppInitDataResponse response = new AppInitDataResponse();

        response.setAgreementsList(agreementResponses);
        response.setStyleList(styleTypeList);
        response.setPlaceList(placeList);
        response.setAgeList(ageList);
        response.setBrandList(brandResponseList);
        return response;
    }

    public List<Agreements> getAppAgreementList(String version) {
        return appDomainService.selectAgreements(version);
    }

}
