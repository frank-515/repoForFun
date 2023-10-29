const {getConfig, digestForSubFolders} = require('./misc')
const path = require("path");

const config = getConfig();
function initMetadata() {
    digestForSubFolders( path.join(config.hosting_directory, './versions'))

}

module.exports = {
   initMetadata
}