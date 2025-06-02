DROP TABLE IF EXISTS
    supplierinventorydb.productsinorder,
    supplierinventorydb.constantorders,
    supplierinventorydb.productInAgreement,
    supplierinventorydb.productOfSupplier,
    supplierinventorydb."order",
    supplierinventorydb.informationcontact,
    supplierinventorydb.discount,
    supplierinventorydb.bank,
    supplierinventorydb.agreement,
    supplierinventorydb.product,
    supplierinventorydb.supplier,
    supplierinventorydb.branch
    CASCADE;

DROP SEQUENCE IF EXISTS
    supplierinventorydb.branch_id_seq,
    supplierinventorydb.order_id_seq,
    supplierinventorydb.product_id_seq,
    supplierinventorydb.supplier_id_seq
    CASCADE;
