const express = require('express');
const router = express.Router();
router.use('/openapi',require('./openapi'));
router.use('/hyd',require('./hyd'));
router.use('/ipos',require('./ipos'));
router.use('/appprotal',require('./appprotal'));

module.exports = router;
