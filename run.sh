#!/bin/sh

/rhasspy/build/install/rhasspy-sphinx4/bin/rhasspy-sphinx4 \
    -a "${ACOUSTIC_MODEL}" -l "${LANGUAGE_MODEL}" -d "${DICTIONARY}"
