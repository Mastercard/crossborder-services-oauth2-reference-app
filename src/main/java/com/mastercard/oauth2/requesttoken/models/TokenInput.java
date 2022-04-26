package com.mastercard.oauth2.requesttoken.models;

import com.nimbusds.jose.JWSAlgorithm;
import lombok.*;

@Data
 @Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenInput {

     private String consumerKey;

     // flag to indicate if token signing certificate chain should be added in token itself while building a token
     private boolean populateX5cTokenHeader = true;
     @Builder.Default
     private int tokenLifetime = 15 * 60 * 1000;

     // token signing algorithm
     @Builder.Default
     private JWSAlgorithm tokenSigningAlgorithm = JWSAlgorithm.RS256;

}
