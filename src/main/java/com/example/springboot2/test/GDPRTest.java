package com.example.springboot2.test;

import com.iab.gdpr.Purpose;
import com.iab.gdpr.consent.VendorConsent;
import com.iab.gdpr.consent.VendorConsentDecoder;
import com.iab.gdpr.consent.VendorConsentEncoder;
import com.iab.gdpr.consent.implementation.v1.VendorConsentBuilder;
import com.iab.gdpr.consent.range.RangeEntry;
import com.iab.gdpr.consent.range.SingleRangeEntry;
import com.iab.gdpr.consent.range.StartEndRangeEntry;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.iab.gdpr.Purpose.STORAGE_AND_ACCESS;

public class GDPRTest {

    final Set<Purpose> allowedPurposes = new HashSet<>(Arrays.asList(Purpose.STORAGE_AND_ACCESS, Purpose.PERSONALIZATION, Purpose.AD_SELECTION, Purpose.CONTENT_DELIVERY, Purpose.MEASUREMENT));
    //final int cmpId = 10;
    //final int cmpVersion = 3;
    //final int consentScreenID = 4;
    final String consentLanguage = "EN";
    final int vendorListVersion = 142;
    final int maxVendorId = 82;
    //0=BitField 1=Range
    final int vendorEncodingType = 1;
    final Set<Integer> allowedVendors = new HashSet<>(Arrays.asList(69, 82));
    final List<RangeEntry> rangeEntries = Arrays.asList(
            new SingleRangeEntry(69),
            //new StartEndRangeEntry(69,82),
            new SingleRangeEntry(82)
    );


    public static void main(String[] args) {
        GDPRTest gdprTest = new GDPRTest();
        System.out.println(gdprTest.vendorConsentEncoder());
        gdprTest.vendorConsentDecoder("BOgDgaoOgDgaoAAAAAENCO-AAAAFKACACKAFIA");
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
                //.withCmpID(cmpId)
                //.withCmpVersion(cmpVersion)
                //.withConsentScreenID(consentScreenID)
                .withConsentLanguage(consentLanguage)
                .withVendorListVersion(vendorListVersion)
                .withAllowedPurposes(allowedPurposes)
                .withMaxVendorId(maxVendorId)
                .withVendorEncodingType(vendorEncodingType)
                .withDefaultConsent(false)
                .withRangeEntries(rangeEntries)
                .withBitField(allowedVendors)
                .build();
        final String base64String = VendorConsentEncoder.toBase64String(vendorConsent);
        return base64String;
    }

}
