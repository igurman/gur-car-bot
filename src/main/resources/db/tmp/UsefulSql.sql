select m.id, m.name, mo.id, mo.name
from vin_make_model vmm
         left join vin_make m on vmm.makeid = m.id
         left join vin_model mo on vmm.modelid = mo.id
where m.name = 'Freightliner'
--and mo.name ilike '%CHASSIS%'
order by mo.name;










