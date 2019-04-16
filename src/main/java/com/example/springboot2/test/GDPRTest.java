package com.example.springboot2.test;

import com.iab.gdpr.consent.VendorConsent;
import com.iab.gdpr.consent.VendorConsentDecoder;
import com.iab.gdpr.consent.VendorConsentEncoder;
import com.iab.gdpr.consent.implementation.v1.VendorConsentBuilder;

import java.time.Instant;

import static com.iab.gdpr.Purpose.STORAGE_AND_ACCESS;

public class GDPRTest {

    public static void main(String[] args) {

    }


    private VendorConsent vendorConsentDecoder(String consentString) {
        int vendorId = 0;

        final VendorConsent vendorConsent = VendorConsentDecoder.fromBase64String(consentString);

        if (vendorConsent.isVendorAllowed(vendorId) && vendorConsent.isPurposeAllowed(STORAGE_AND_ACCESS)) {
           //todo
        } else {
           //todo
        }

        return null;
    }


    private String vendorConsentEncoder() {
        final VendorConsent vendorConsent = new VendorConsentBuilder()
                .withConsentRecordCreatedOn(Instant.now())
                .withConsentRecordLastUpdatedOn(Instant.now())
                /*.withCmpID(cmpId)
                .withCmpVersion(cmpVersion)
                .withConsentScreenID(consentScreenID)
                .withConsentLanguage(consentLanguage)
                .withVendorListVersion(vendorListVersion)
                .withAllowedPurposes(allowedPurposes)
                .withMaxVendorId(maxVendorId)
                .withVendorEncodingType(vendorEncodingType)
                .withDefaultConsent(false)
                .withRangeEntries(rangeEntries)*/
                .build();
        final String base64String = VendorConsentEncoder.toBase64String(vendorConsent);
        return base64String;
    }

}
