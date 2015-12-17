#!/bin/bash

# Generate merchant-key.pem file:
openssl ecparam -name prime256v1 -genkey -noout -out merchant-key.pem

# Print public and private key in hex form:
openssl ec -in merchant-key.pem -pubout -text -noout

# Print further instructions
echo ""
echo "                              IMPORTANT                                      "
echo "============================================================================="
echo "Copy the value of 'pub' above into a variable named PUBLICKEY and then run:"
echo "echo \$PUBLICKEY | xxd -r -p | base64"
echo ""
echo "You should see output like:"
echo "BO39Rh43UGXMQy5PAWWe7UGWd2a9YRjNLPEEVe+zWIbdIgALcDcnYCuHbmrrzl7h8FZjl6RCzoi5/cDrqXNRVSo="
echo "============================================================================="
