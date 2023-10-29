

const express = require('express');
const router = express.Router();

const { parseJsonFromFile } = require('../misc')
const path = require('path')

const getVersionList = () => { return parseJsonFromFile(path.join(__dirname, "../public/data/versions/test.json"));}

router.get('/version', function(req, res) {
    const versionList = getVersionList
    for (const versionData of versionList.metadata) {
        if (versionData.version === versionList.latest) {
            res.json(versionData)
            return
        }
    }
    res.json({})
});

router.get('/version/latest', function(req, res) {
    res.redirect("/check/version");
});

router.get('/version/:version', function(req, res) {
    const versionList = getVersionList
    for (const versionData of versionList.metadata) {
        if (versionData.version === req.params.version) {
            res.json(versionData)
            return
        }
    }
    res.json({})
});
router.get('/list', function(req, res) {
    const versionList = getVersionList
    let versionNumbers = []

    for (const version of versionList.metadata) {
        versionNumbers.push(version.version);
    }
    versionNumbers.sort().reverse()
    res.json({versions: versionNumbers})
});


module.exports = router;
